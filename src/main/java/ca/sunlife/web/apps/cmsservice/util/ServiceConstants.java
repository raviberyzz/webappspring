package ca.sunlife.web.apps.cmsservice.util;

public class ServiceConstants {

	public static final String NAME_REGEXP = "^[a-zA-ZàâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ][a-zA-ZàâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ\\.'’() -]{0,80}$";
    public static final String EMAIL_REGEXP= "^(([\\w!#$%&'+\\-\\/=?\\^`{|}~]+([\\.]?[\\w!#$%&'*+\\-\\/=?\\^`{|}~]+)*)|(\\\"[\\w!#$%&'*+\\-\\/=?\\^`{|}~\"(),:;\\\\<>@\\[\\]]+\\\"))@(([a-zA-Z0-9]+([\\-]?[a-zA-Z0-9]+)*(\\.[a-zA-Z]{2,})+)|(\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])){0,80}$";
    public static final String LANG_REGEXP = "^[a-zA-ZÀ-ÿ-. ]*$";
    public static final String LANGUAGE_REGEXP = "^(?:English|French)$";
    public static final String POSTAL_REGEXP = "^([a-zA-Z][0-9][a-zA-Z](?:\\s|\\-)?[0-9][a-zA-Z][0-9])$|^([a-zA-Z][0-9][a-zA-Z])$";
    public static final String DOB_REGEXP = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$";
    public static final String CUR_VALIDATION_REGEXP = "^\\d{1,18}$";
    
    public static final String FAA_EMAIL_REGEXP = "^(([\\w!#$%&'+\\-\\/=?\\^`{|}~]+([\\.]?[\\w!#$%&'*+\\-\\/=?\\^`{|}~]+)*)|(\\\"[\\w!#$%&'*+\\-\\/=?\\^`{|}~\"(),:;\\\\<>@\\[\\]]+\\\"))@(([a-zA-Z0-9]+([\\-]?[a-zA-Z0-9]+)*(\\.[a-zA-Z]{2,})+)|(\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])){0,80}$";
    public static final String FAA_NAME_REGEXP = "^[a-zA-ZàâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ][a-zA-ZàâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ\\.'’() -]{0,40}$";
    public static final String FAA_PHONE_REGEXP = "^\\d\\d\\d-\\d\\d\\d-\\d\\d\\d\\d$";
    public static final String FAA_POSTAL_REGEXP = "^[A-Za-z]\\d[A-Za-z][ ]?\\d[A-Za-z]\\d$";
    public static final String FAA_ALT_LANG_REGEXP = "^[A-Za-z\\s()!\\?\\-,]+$";
    public static final String FAA_INSTRUCTIONS_REGEXP = "^[A-Za-z0-9çéâêîôûàèìòùäëïüæœÇÉÀÈÌÒÙÂÊÎÔÛÄËÏÜÆŒ\\s()!\\?\\-,!\\\"#\\\\$%&'()*+\\,\\-\\./:;<=>?@\\[\\]^_\\{\\|\\}~©®²³¶¹¿Þˆ™]{0,32000}$";
    public static final String FAA_EXTENSION_REGEXP = "^[0-9]+$";
    public static final String FAA_LEAD_SOURCE_REGEXP = "^[a-zA-ZàâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ][a-zA-ZàâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ\\.'’() -]{0,80}$";
    public static final String FAA_DATE_TIME_REGEXP = "^\\d\\d\\d\\d-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}$";
    public static final String FAA_CITY_REGEXP = "^[a-zA-ZàâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ][a-zA-ZàâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ\\.'’() -]{0,40}$";
    public static final String FAA_MARKETING_REGEXP = "^[a-zA-Z0-9àâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ][a-zA-Z0-9àâäèéêëîïôœùûüÿçÀÂÄÈÉÊËÎÏÔŒÙÛÜŸÇ_ -:]{0,80}$";
    public static final String FAA_ANALYTICS_ID_REGEXP = "^[a-zA-Z0-9 -]{0,50}$";
    public static final String FAA_SUBMISSION_ID_REGEXP = "^[a-zA-Z0-9 -]{0,100}$";
    public static final String FAA_TRAFFIC_REGEXP = "^[a-zA-Z_ -]{0,255}$";
    public static final String FAA_PROVINCE_REGEXP = "^[a-zA-Z]{0,2}$";
    public static final String FAA_PARTY_ID_REGEXP = "^[0-9]{0,10}$";
    
    private ServiceConstants() {}
}
