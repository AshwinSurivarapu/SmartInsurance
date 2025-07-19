# smartinsure-platform/services/python/plan-recommender-service/app.py
from flask import Flask, request, jsonify
from transformers import pipeline # Import pipeline
from pydantic import BaseModel #ensure basemodel is impoeted if using flask with pydantic
app = Flask(__name__)

# --- Hugging Face Model Loading ---
# Load the zero-shot classification pipeline once when the app starts
print("Loading Hugging Face zero-shot-classification pipeline...")
try:
    # This model is good for zero-shot classification.
    # It might take some time to download the first time.
    classifier = pipeline("zero-shot-classification", model="facebook/bart-large-mnli")
    print("Hugging Face zero-shot-classification pipeline loaded successfully.")
except Exception as e:
    print(f"Error loading Hugging Face pipeline: {e}")
    classifier = None # Indicate failure to load

# --- DTOs (Keep UserDetails and ApplicationPayload as is) ---
class UserDetails(BaseModel):
    username: str
    age: int
    income: float

class ApplicationPayload(BaseModel):
    userDetails: UserDetails

# --- UPDATED: RecommendationResponse to include three ranked options ---
class RecommendationResponse(BaseModel):
    best_option: str
    better_option: str
    good_option: str
    message: str = "Recommendation generated successfully."
    # You might also want to include scores if the frontend needs them
    # best_option_score: float = 0.0
    # better_option_score: float = 0.0
    # good_option_score: float = 0.0

@app.route('/')
def home():
    return "Plan Recommender Service is running!"

@app.route('/recommend', methods=['POST'])
def recommend_plan():
    if classifier is None:
        return jsonify({"error": "AI model not loaded. Service unavailable."}), 503

    data = request.get_json()

    if not data:
        return jsonify({"error": "No input data provided"}), 400

    user_details = data.get('userDetails', {})
    print(f"Received user details for recommendation: {user_details}")

    # --- Zero-Shot Classification Logic ---
    # Construct a descriptive text about the user from the incoming details.
    # This text will be fed to the zero-shot classification model.
    sequence_to_classify = (
        f"Customer {user_details.get('username', 'N/A')} "
        f"is {user_details.get('age', 'N/A')} years old "
        f"with an income of ${user_details.get('income', 'N/A')}. "
        "They are exploring insurance options for their future and assets."
    )

    # Define your candidate insurance plan labels.
    # You can customize these to match your actual insurance products.
    candidate_labels = [
        "Life Insurance",
        "Health Insurance",
        "Car Insurance",
        "Home Insurance",
        "Travel Insurance",
        "Retirement Plan",
        "Critical Illness Cover",
        "Personal Accident Cover"
    ]

    try:
        # Perform zero-shot classification
        # The model will return a list of labels with their confidence scores,
        # sorted by score in descending order.
        results = classifier(sequence_to_classify, candidate_labels, multi_label=False)
        print(f"Zero-shot classification results: {results}")

        num_options=len(results['labels'])
        best_opt="N/A"
        better_opt="N/A"
        good_opt="N/A"
        best_opt_score= 0.0
        if num_options>0:
            best_opt=results['labels'][0]
            best_opt_score=results['scores'][0]
        if num_options>1:
            better_opt=results['labels'][1]
        if num_options>2:
            good_opt=results['labels'][2]
        # The most confident recommendation is the first one in the sorted list
        message = f"AI provided ranked recommendations based on profile. Best confidence: {best_opt_score:.2f}"

        # --- CRITICAL CHANGE HERE ---
        # 1. Create the RecommendationResponse Pydantic object
        response_model_instance = RecommendationResponse(
            best_option=best_opt,
            better_option=better_opt,
            good_option=good_opt,
            message=message
        )
        # 2. Convert the Pydantic model instance to a Python dictionary using .model_dump()
        response_data_dict = response_model_instance.model_dump()

        # 3. Use Flask's jsonify to return the dictionary as a JSON response with the correct header
        return jsonify(response_data_dict), 200
    except Exception as e:
        print(f"Error during AI inference: {e}")
        return jsonify({"error": f"Failed to generate AI recommendation: {e}"}), 500

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5001)