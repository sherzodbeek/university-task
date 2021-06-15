package uz.pdp.appjparelationships.payload;

import lombok.Data;

import java.util.List;

@Data
public class StudentDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private Integer addressId;
    private Integer groupId;
    private List<Integer> subjectsId;
}
