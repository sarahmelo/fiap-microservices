package br.com.fiap.aspersor.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_sprinkler")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Sprinkler {
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "seq_sprinkler_id"
    )
    @SequenceGenerator(
            name = "seq_sprinkler_id",
            sequenceName = "seq_sprinkler_id",
            allocationSize = 1
    )
    private Long id;

    private String name;

    @Column(nullable = false)
    private String location;

    @Column(name = "status")
    private String status;

    @Column(name = "operation_mode")
    private String operationMode;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
}
