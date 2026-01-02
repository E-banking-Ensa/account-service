package com.ebanking.accountservice.dto.response;


public class VirementDetailsDTO {

    private String motif;
    private String destinationRib;
    private String typeVirement;

    public VirementDetailsDTO() {}

    public VirementDetailsDTO(String motif, String destinationRib, String typeVirement) {
        this.motif = motif;
        this.destinationRib = destinationRib;
        this.typeVirement = typeVirement;
    }

    // getters & setters

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getDestinationRib() {
        return destinationRib;
    }

    public void setDestinationRib(String destinationRib) {
        this.destinationRib = destinationRib;
    }

    public String getTypeVirement() {
        return typeVirement;
    }

    public void setTypeVirement(String typeVirement) {
        this.typeVirement = typeVirement;
    }
}
