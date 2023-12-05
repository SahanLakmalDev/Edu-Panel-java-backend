package lk.ijse.dep11.edupanel.to.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LecturerReqTO {
    @NotBlank(message = "Name can't be empty")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Invalid name")
    private String name;
    @NotBlank(message = "Designation can't be empty")
    @Length(min = 2, message = "Invalid designation")
    private String designation;
    @NotBlank(message = "Designation can't be empty")
    @Length(min = 2, message = "Invalid designation")
    private String qualifications;
    @NotBlank(message = "Designation can't be empty")
    @Pattern(regexp = "^(full-time|part-time)$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Invalid type")
    private String type;
    private MultipartFile picture;
    @Pattern(regexp = "^http[s]?://.+$", message = "Invalid linkedin url")
    private String linkedin;
}
