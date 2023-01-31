package ca.sunlife.web.apps.cmsservice.util;

public class ServiceConstants {

	public static final String NAME_REGEXP = "^[a-zA-ZàâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ][a-zA-ZàâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ\\.'’() -]{0,40}";
    public static final String EMAIL_REGEXP= "^(([\\w!#$%&'+\\-\\/=?\\^`{|}~]+([\\.]?[\\w!#$%&'*+\\-\\/=?\\^`{|}~]+)*)|(\\\"[\\w!#$%&'*+\\-\\/=?\\^`{|}~\"(),:;\\\\<>@\\[\\]]+\\\"))@(([a-zA-Z0-9]+([\\-]?[a-zA-Z0-9]+)*(\\.[a-zA-Z]{2,})+)|(\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])){0,80}$";
    public static final String LANG_REGEXP = "^[a-zA-ZÀ-ÿ-. ]*$";
    public static final String POSTAL_REGEXP = "^[a-zA-Z][0-9][a-zA-Z](?:\\s|\\-)?[0-9][a-zA-Z][0-9]{0,20}";
    public static final String DOB_REGEXP = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))";
    public static final String CUR_VALIDATION_REGEXP = "^\\d{1,7}(\\.\\d{1,2})?$";
}
