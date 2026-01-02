package com.ebanking.accountservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Virement extends Transaction {
    @ManyToOne
    private Account Source;
    @ManyToOne
    private Account Destination;
    private String Motife;
    @Enumerated(EnumType.STRING)
    private VirementType type_virement;


    public Account getSource() {
        return Source;
    }

    public void setSource(Account source) {
        Source = source;
    }

    public Account getDestination() {
        return Destination;
    }

    public void setDestination(Account destination) {
        Destination = destination;
    }

    public String getMotife() {
        return Motife;
    }

    public void setMotife(String motife) {
        Motife = motife;
    }

    public VirementType getType_virement() {
        return type_virement;
    }

    public void setType_virement(VirementType type_virement) {
        this.type_virement = type_virement;
    }
}