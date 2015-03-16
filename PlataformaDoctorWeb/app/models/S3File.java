package models;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import plugins.S3Plugin;

import javax.persistence.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

@Entity
public class S3File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bucket;

    private String name;

    @ManyToOne
    private Usuario owner;

    @Transient
    private File file;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Usuario getOwner() {
        return owner;
    }

    public void setOwner(Usuario owner) {
        this.owner = owner;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public URL getUrl(){
        try {
            return new URL("https://s3.amazonaws.com/" + bucket + "/" + getActualFileName());
        } catch (MalformedURLException e) {
            return null;
        }
    }

    private String getActualFileName() {
        return "users/" + owner.getId() + "/" + name;
    }

    @Transactional
    public static void save(S3File nFile) throws RuntimeException {
        if (S3Plugin.amazonS3 == null) {
            Logger.error("Could not save because amazonS3 was null");
            throw new RuntimeException("Could not save");
        }
        else {
            nFile.bucket = S3Plugin.s3Bucket;
            JPA.em().persist(nFile); // assigns an id
            PutObjectRequest putObjectRequest = new PutObjectRequest(nFile.bucket, nFile.getActualFileName(), nFile.getFile());
            putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead); // public for all
            S3Plugin.amazonS3.putObject(putObjectRequest); // upload file
        }
    }

    @Transactional
    public static void delete(S3File nFile) {
        if (S3Plugin.amazonS3 == null) {
            Logger.error("Could not delete because amazonS3 was null");
            throw new RuntimeException("Could not delete");
        }
        else {
            S3Plugin.amazonS3.deleteObject(nFile.bucket, nFile.getActualFileName());
            JPA.em().remove(nFile);
        }
    }
}