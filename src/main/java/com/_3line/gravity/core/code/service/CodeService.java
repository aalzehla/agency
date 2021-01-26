package com._3line.gravity.core.code.service;


import com._3line.gravity.core.code.dto.CodeDTO;
import com._3line.gravity.core.code.model.Code;
import com._3line.gravity.core.code.model.CodeType;
import com._3line.gravity.core.exceptions.GravityException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * This {@code CodeService} interface provides the methods for managing system
 * codes. A {@code Code}can be used add new items to the system which can be
 * displayed in list or in drop down menus
 *

 */
public interface CodeService {

    @PreAuthorize("hasAuthority('ADD_CODE')")
    String addCode(CodeDTO code) throws GravityException;

    Page<CodeType> findByCodeType(String codeType, Pageable pageDetails);

    /**
     * Deletes a code from the system
     *
     * @param codeId the oode's id
     */
    @PreAuthorize("hasAuthority('DELETE_CODE')")
    String deleteCode(Long codeId) throws GravityException;

    /**
     * Returns the specified code
     *@param codeId the code's id
     *
     * @return the code
     */

    @PreAuthorize("hasAuthority('GET_CODE')")
    CodeDTO getCode(Long codeId);

    @PreAuthorize("hasAuthority('GET_CODE')")
    Code getCodeById(Long codeId);

    /**
     * Returns a list of codes specified by the given type
     *@param codeType the code's type
     *
     * @return a list of codes
     */
    List<CodeDTO> getCodesByType(String codeType);

    @PreAuthorize("hasAuthority('UPDATE_CODE')")
    public String updateCode(CodeDTO codeDTO) throws GravityException;

    @PreAuthorize("hasAuthority('VIEW_CODES')")
    Page<CodeDTO> getCodesByType(String codeType, Pageable pageDetails);

    @PreAuthorize("hasAuthority('VIEW_CODES')")
    Page<CodeType> getCodeTypes(Pageable pageDetails);

    public Code getByTypeAndCode(String type, String code);


    Page<CodeDTO> findByTypeAndValue(String codeType, String value, Pageable pageDetails);

    @PreAuthorize("hasAuthority('VIEW_CODES')")
    Page<CodeDTO> getCodes(Pageable pageDetails);

    /**
     * Returns all the codes in the system
     *
     * @return a list of the codes
     */
    Iterable<CodeDTO> getCodes();

    CodeDTO convertEntityToDTO(Code code);

    Code convertDTOToEntity(CodeDTO codeDTO);

    List<CodeDTO> convertEntitiesToDTOs(Iterable<Code> codes);


}