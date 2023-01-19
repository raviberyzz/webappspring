package ca.sunlife.web.apps.cmsservice.util;

public class ServiceConstants {

	public static final String NAME_REGEXP = "^[a-zA-ZàâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ][a-zA-ZàâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ\\.'’() -]*";
    public static final String EMAIL_REGEXP= "^(([\\w!#$%&'+\\-\\/=?\\^`{|}~]+([\\.]?[\\w!#$%&'*+\\-\\/=?\\^`{|}~]+)*)|(\\\"[\\w!#$%&'*+\\-\\/=?\\^`{|}~\"(),:;\\\\<>@\\[\\]]+\\\"))@(([a-zA-Z0-9]+([\\-]?[a-zA-Z0-9]+)*(\\.[a-zA-Z]{2,})+)|(\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]))$";
    public static final String LANG_REGEXP = "^[a-zA-Z ]+";
    public static final String POSTAL_REGEXP = "^[a-zA-Z][0-9][a-zA-Z](?:\\s|\\-)?[0-9][a-zA-Z][0-9]+";
    public static final String DOB_REGEXP = "^[0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((?:19|20)[0-9][0-9]]+";
}
