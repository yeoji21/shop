package delivery.shop.file.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class File {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Embedded
    private FileName fileName;

    private String filePath;

    @Builder
    public File(FileName fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
