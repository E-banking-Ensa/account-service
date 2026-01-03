package com.ebanking.accountservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VirementDetailsDTO {

    private String motif;
    private String destinationRib;
    private String typeVirement;

}
