package com.tuse.tuse.requests.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UpdateCompanyRequest {

    private Long companyId;
    private String name;
    private String symbol;
    private String sector;
}
