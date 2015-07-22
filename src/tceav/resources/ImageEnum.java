/*
 * ImageEnum.java
 *
 * Created on 10 January 2008, 10:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.resources;

/**
 *
 * @author nzr4dl
 */
public enum ImageEnum {
    
    aclAccessorType("images/acl/types_16.png"),
    aclAccessorID("images/acl/user_16.png"),
    aclAdministerADALicense("images/acl/license_16.png"),
    aclAssignToProject("images/acl/assign_to_project_16.png"),
    aclBatchPrint("images/acl/am_batchprint_access_16.png"),
    aclChange("images/acl/changeaccess_16.png"),
    aclChangeOwnership("images/acl/changeownership_16.png"),
    aclClassification("images/acl/classified_16.png"),
    aclCheckInCheckOut("images/acl/cico_16.png"),
    aclCopy("images/acl/copy_16.png"),
    aclDelete("images/acl/deleteaccess_16.png"),
    aclDemote("images/acl/demoteaccess_16.png"),
    aclDigitalSign("images/acl/am_digital_sign16.png"),
    aclExport("images/acl/exportaccess_16.png"),
    aclImport("images/acl/importaccess_16.png"),
    aclIPAdmin("images/acl/Intellectual_property_16.png"),
    aclITARAdmin("images/acl/ITAR_admin_16.png"),
    aclMarkUp("images/acl/am_priviledge_markup_16.png"),
    aclPromote("images/acl/promoteaccess_16.png"),
    aclPublish("images/acl/publish_16.png"),
    aclRead("images/acl/readaccess_16.png"),
    aclRemoteCICO("images/acl/remote_cico_16.png"),
    aclRemoveFromProject("images/acl/remove_from_project_16.png"),
    aclSubscribe("images/acl/subscribe_16.png"),
    aclTransferIn("images/acl/unreserve_16.png"),
    aclTransferOut("images/acl/reserve_16.png"),
    aclTranslation("images/acl/localization_values_16.png"),
    aclUnmanage("images/acl/unmanaged_16.png"),
    aclWrite("images/acl/writeaccess_16.png"),
    aclUndefined("images/acl/error_16.png"),

    amNo("images/accessmanager/no_16.png"),
    amNamedAclType("images/accessmanager/namedacl_16.png"),
    amRule("images/accessmanager/amtreerule_16.png"),
    amRuleBranch("images/accessmanager/item_16.png"),
    amRuletree("images/accessmanager/ruletree_16.png"),
    amSystemNamedAclType("images/accessmanager/systemnamedacl_16.png"),
    amWorkflowType("images/accessmanager/workflownamedacl_16.png"),
    amYes("images/accessmanager/yes_16.png"),
    amCompare("images/accessmanager/bid_package_rev_16.png"),
    
    amcmpEqual("images/compare/equal_16.png"),
    amcmpNotEqual("images/compare/not_equal_16.png"),
    amcmpNotFound("images/compare/not_found_16.png"),
    
    
    pmTabulate("images/proceduremanager/calendar.png"),
    pmTabulateExport("images/proceduremanager/showimplementedby_16.png"),
    
    pmAssociatedDataSet("images/proceduremanager/dataset_16.png"),
    pmAssociatedFolder("images/proceduremanager/foldertype_16.png"),
    pmAssociatedForm("images/proceduremanager/formtype_16.png"),
    pmArgument("images/proceduremanager/addpartotproduct_16.png"),
    pmBusinessRule("images/proceduremanager/rulehandler_16.png"),
    pmBusinessRuleHandler("images/proceduremanager/rule_16.png"),
    pmOrganisation("images/proceduremanager/organization_16.png"),
    pmRole("images/proceduremanager/role_16.png"),
    pmSignOffProfile("images/proceduremanager/performsignofftask_16.png"),
    pmSite("images/proceduremanager/site_16.png"),
    pmUserData("images/proceduremanager/architecture_16.png"),
    pmUserValue("images/proceduremanager/architecturerevision_16.png"),
    pmWorkflow("images/proceduremanager/processstate_16.png"),
    pmWorkflowAction("images/proceduremanager/actionhandler_16.png"),
    pmWorkflowHandler("images/proceduremanager/handlers_16.png"),
    pmValidationChecker("images/proceduremanager/validation_16.png"),
    pmValidationResults("images/proceduremanager/configfile_sw_16.png"),
    pmXML("images/proceduremanager/showrealizedby_16.png"),
    
    workflowAcknowledge("images/workflow/acknowledgetask_16.png"),
    workflowAddStatus("images/workflow/addstatustasknode_16.png"),
    workflowChecklist("images/workflow/checklist_16.png"),
    workflowCondition("images/workflow/conditiontask_16.png"),
    workflowDo("images/workflow/dotask_16.png"),
    workflowImpactAnalysis("images/workflow/impactanalysis_16.png"),
    workflowNotify("images/workflow/notifytasknode_16.png"),
    workflowOr("images/workflow/ortasknode_16.png"),
    workflowPerformSignOff("images/workflow/performsignofftask_16.png"),
    workflowPrePareECO("images/workflow/prepareeco_16.png"),
    workflowProcess("images/workflow/process_16.png"),
    workflowReviewProcess("images/workflow/processvariable_16.png"),
    workflowReview("images/workflow/reviewtask_16.png"),
    workflowRoute("images/workflow/routetasknode_16.png"),
    workflowSelectSignOff("images/workflow/selectsignofftask_16.png"),
    workflowSync("images/workflow/synctask_16.png"),
    workflowTask("images/workflow/task_16.png"),
    workflowTaskDependancies("images/workflow/taskdependencies_16.png"),
    
    
    utilClear("images/util/clear_16.png"),
    utilClose("images/util/exitmpp_16.png"),
    utilCollapse("images/util/collapsetree_16.png"),
    utilCollapseAll("images/util/collapse_16.png"),
    utilExpand("images/util/expandtree_16.png"),
    utilExpandAll("images/util/expand_16.png"),
    utilExit("images/util/exit_16.png"),
    utilFind("images/util/find_16.png"),
    utilCompare("images/util/bid_package_rev_16.png"),
    utilClipboardCopy("images/util/copy_16.png"),

    appLogo("images/app/logo.png"),
    appIcon("images/app/icon.png"),
    appLogoBanner("images/app/logoBanner.png");
    
    
    private final String value;

    ImageEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }
}
