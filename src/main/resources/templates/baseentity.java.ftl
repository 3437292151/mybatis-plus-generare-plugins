package ${package.BaseEntity};

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Contract for a common primary key super class.
 *
 */
@Data
public class PrimaryKey implements Serializable{

    private String id;

    private LocalDateTime defDt;

    private LocalDateTime updDt;

    public PrimaryKey setUUID(){
        this.setId(UUID.randomUUID().toString());
        return this;
    }

    public PrimaryKey setTime(){
        this.setDefDt(LocalDateTime.now());
        this.setUpdDt(LocalDateTime.now());
        return this;
    }
}

