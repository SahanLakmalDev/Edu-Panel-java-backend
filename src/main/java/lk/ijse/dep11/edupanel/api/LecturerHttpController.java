package lk.ijse.dep11.edupanel.api;

import lk.ijse.dep11.edupanel.to.request.LecturerReqTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/lecturers")
@CrossOrigin
public class LecturerHttpController {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public void createNewLecturer(@ModelAttribute @Valid LecturerReqTO lecturer){
        System.out.println(lecturer);
        System.out.println("createNewLecturer()");





    }
    @PatchMapping("/{lecturer_id}")
    public void updateLecturerDetails(){
        System.out.println("updateLecturerDetails()");
    }
    @DeleteMapping("/{lecturer_id}")
    public void deleteLecturer(){
        System.out.println("deleteLecturer()");
    }
    @GetMapping
    public void getAllLecturers(){
        System.out.println("getAllLecturers()");
    }

}
