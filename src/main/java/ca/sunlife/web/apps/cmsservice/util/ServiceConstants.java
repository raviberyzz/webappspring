package ca.sunlife.web.apps.cmsservice.util;

public class ServiceConstants {

	public static final String NAME_REGEXP = "^[a-zA-ZàâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ][a-zA-ZàâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ\\.'’() -]{0,80}$";
    public static final String EMAIL_REGEXP= "^(([\\w!#$%&'+\\-\\/=?\\^`{|}~]+([\\.]?[\\w!#$%&'*+\\-\\/=?\\^`{|}~]+)*)|(\\\"[\\w!#$%&'*+\\-\\/=?\\^`{|}~\"(),:;\\\\<>@\\[\\]]+\\\"))@(([a-zA-Z0-9]+([\\-]?[a-zA-Z0-9]+)*(\\.[a-zA-Z]{2,})+)|(\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])){0,80}$";
    public static final String LANG_REGEXP = "^[a-zA-ZÀ-ÿ-. ]*$";
    public static final String LANGUAGE_REGEXP = "^(?:English|French)$";
    public static final String POSTAL_REGEXP = "^([a-zA-Z][0-9][a-zA-Z](?:\\s|\\-)?[0-9][a-zA-Z][0-9])$|^([a-zA-Z][0-9][a-zA-Z])$";
    public static final String DOB_REGEXP = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
    public static final String CUR_VALIDATION_REGEXP = "^\\d{1,18}$";

    private ServiceConstants() {}
}
