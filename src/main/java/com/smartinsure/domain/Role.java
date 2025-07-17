package com.smartinsure.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="users")
@Data
@NoArgsConstructor
public class Role {
    @Id
    private String id;

   private String name;

    private Role(String name){
        this.name=name;
    }
}
