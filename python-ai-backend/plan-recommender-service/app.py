# smartinsure-platform/services/python/plan-recommender-service/app.py
from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/')
def home():
    return "Plan Recommender Service is running!"

@app.route('/recommend', methods=['POST']) # Handles POST requests to /recommend
def recommend_plan():
    # Get JSON data from the request body
    data = request.get_json()

    if not data:
        return jsonify({"error": "No input data provided"}), 400

    user_details = data.get('userDetails', {}) # Expecting a 'userDetails' key in the input
    # For now, we'll just print them and return a hardcoded recommendation
    print(f"Received user details for recommendation: {user_details}")

    # --- Hardcoded Recommendation Logic ---
    # In Day 3, we'll integrate Hugging Face here.
    # For demonstration, let's pretend we're recommending based on age or income
    user_age = user_details.get('age', 0)
    user_income = user_details.get('income', 0)

    if user_age >= 50 or user_income >= 100000:
        recommended_plan = "Premium Health Insurance Plan"
    else:
        recommended_plan = "Standard Basic Insurance Plan"
    # --- End Hardcoded Logic ---

    response = {
        "recommendation": recommended_plan,
        "message": "This is a hardcoded recommendation for now."
    }
    return jsonify(response), 200 # 200 OK status

if __name__ == '__main__':
    # Ensure you are running in your virtual environment
    app.run(debug=True, host='0.0.0.0', port=5001) # Host '0.0.0.0' makes it accessible from outside localhost