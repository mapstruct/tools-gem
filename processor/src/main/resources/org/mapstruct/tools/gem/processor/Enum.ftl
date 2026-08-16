<#--

    Copyright MapStruct Authors.

    Licensed under the Apache License version 2.0, available at https://www.apache.org/licenses/LICENSE-2.0

-->
<#-- @ftlvariable name="gemEnum" type="org.mapstruct.tools.gem.processor.GemEnum" -->
package ${gemEnum.gemPackageName};

/**
 * Gem for the enum {@link ${gemEnum.originalEnumFullName}}
*/
public enum ${gemEnum.gemName} {
<#list gemEnum.enumConstants as enumConstant>
    ${enumConstant}<#if enumConstant?has_next>,</#if>
</#list>
}