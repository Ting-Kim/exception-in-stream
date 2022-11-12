package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptedObject {

    private Long id;
    private Object origin;
    private String exceptionMessage;

    @Override
    public String toString() {
        return "ExceptedObject{" +
                "id=" + id +
                ", origin=" + origin +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                '}';
    }
}
