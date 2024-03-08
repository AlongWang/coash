package com.along.coash.server.admin.contract.codegen;

import lombok.Data;

import java.util.List;

@Data
public class CodegenDetailResponse {

    private CodegenTableResponse table;

    private List<CodegenColumnResponse> columns;

}
