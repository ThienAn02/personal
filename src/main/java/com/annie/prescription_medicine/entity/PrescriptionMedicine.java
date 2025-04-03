package com.annie.prescription_medicine.entity;

import com.annie.medicine.entity.Medicine;
import com.annie.prescription.entity.Prescription;
import jakarta.persistence.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PrescriptionMedicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne
    @JoinColumn(name="medicine_id", nullable=false)
    private Medicine medicine;

    @ManyToOne
    @JoinColumn(name="prescription_id", nullable=false)
    private Prescription prescription;

    @Column(nullable = false)
    private Integer dosage;

    @Column(nullable = false)
    private Integer duration;

}
