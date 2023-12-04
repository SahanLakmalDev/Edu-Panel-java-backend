package lk.ijse.dep11.edupanel.api;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import lk.ijse.dep11.edupanel.to.request.LecturerReqTO;
import lk.ijse.dep11.edupanel.to.response.LecturerResTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/lecturers")
@CrossOrigin
public class LecturerHttpController {
    @Autowired
    private DataSource pool;
    @Autowired
    private Bucket bucket;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "multipart/form-data", produces = "application/json")
    public LecturerResTO createNewLecturer(@ModelAttribute @Valid LecturerReqTO lecturer){

        System.out.println(lecturer);
        System.out.println("createNewLecturer()");

        try(Connection connection = pool.getConnection()){
            connection.setAutoCommit(false);
            try{
                PreparedStatement stmInsertLecturer = connection.prepareStatement("INSERT INTO lecturer (name, designation, qualifications, linkedin) VALUES (?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS);
                stmInsertLecturer.setString(1, lecturer.getName());
                stmInsertLecturer.setString(2, lecturer.getDesignation());
                stmInsertLecturer.setString(3, lecturer.getQualifications());
                stmInsertLecturer.setString(4, lecturer.getLinkedin());

                stmInsertLecturer.executeUpdate();
                ResultSet generatedKeys = stmInsertLecturer.getGeneratedKeys();
                generatedKeys.next();

                int lecturerId = generatedKeys.getInt("id");
                String picture = lecturerId + "." + lecturer.getName();

                if(lecturer.getPicture() != null || lecturer.getPicture().isEmpty()){
                    PreparedStatement pstm = connection.prepareStatement("Update lecturer SET picture = ? WHERE id=?");
                    pstm.setString(1, picture);
                    pstm.setInt(2, lecturerId);
                    pstm.executeUpdate();

                }
                final String table = lecturer.getType().equalsIgnoreCase("full-time") ? "full_time_rank" : "part_time_rank";

                Statement stm = connection.createStatement();
                ResultSet rst = stm.executeQuery("SELECT `rank` FROM "+ table +" ORDER BY `rank` DESC LIMIT 1");
                int rank;
                if (!rst.next()) rank = 1;
                else rank = rst.getInt("rank") + 1;
                PreparedStatement stmInsertRank = connection
                        .prepareStatement("INSERT INTO "+ table +" (lecturer_id, `rank`) VALUES (?, ?)");
                stmInsertRank.setInt(1, lecturerId);
                stmInsertRank.setInt(2, rank);
                stmInsertRank.executeUpdate();

                String pictureUrl = null;
                if(lecturer.getPicture() != null || !lecturer.getPicture().isEmpty()){
                    Blob blob = bucket.create(picture, lecturer.getPicture().getInputStream(),lecturer.getPicture().getContentType());
                    pictureUrl = blob.signUrl(1, TimeUnit.DAYS, Storage.SignUrlOption.withV4Signature()).toString();

                }
                connection.commit();
                return new LecturerResTO(lecturerId, lecturer.getName(), lecturer.getDesignation(), lecturer.getQualifications(), lecturer.getType(), pictureUrl, lecturer.getLinkedin());

            }catch (Throwable t){
                connection.rollback();
                throw t;
            }finally {
            connection.setAutoCommit(true);
        }
        }catch (SQLException | IOException e){
            throw new RuntimeException(e);
        }
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
