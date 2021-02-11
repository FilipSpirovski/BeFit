package mk.ukim.finki.befit.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String name;

    private String type;

    @Column(length = 1000)
    private byte[] pictureBytes;

    public Image(String name, String type, byte[] pictureBytes) {
        this.name = name;
        this.type = type;
        this.pictureBytes = pictureBytes;
    }
}
