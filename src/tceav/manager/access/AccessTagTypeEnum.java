/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tceav.manager.access;

/**
 *
 * @author nzr4dl
 */
public enum AccessTagTypeEnum {

    UNDEFINED("Undefined"),

    TcDataAccessConfig("Tc_data_access_config"),
    NamedACLs("named_acls"),
    NamedACL("named_acl"),
    ACLName("acl_name"),
    ACEEntry("ace_entry"),
    AccessorType("accessor_type"),
    Accessor("accessor"),
    Checker("Checker"),
    Grant("grant"),
    Revoke("revoke"),
    RuleTree("rule_tree"),
    TreeNode("tree_node"),
    RuleName("rule_name"),
    RuleArgument("rule_argument"),
    ACEEntryColumn("p");


    private final String value;

    AccessTagTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AccessTagTypeEnum fromValue(String v) {
        for (AccessTagTypeEnum c: AccessTagTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }

        return UNDEFINED;
        //throw new IllegalArgumentException(v.toString());
    }
}
