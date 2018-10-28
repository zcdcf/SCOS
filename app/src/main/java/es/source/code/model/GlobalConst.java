package es.source.code.model;

public class GlobalConst {
    public static final String INFO_ENTRY_TO_MAIN_SCREEN = "FromEntry";
    public static final String INFO_RETURN_TO_MAINSCREEN_FROM_LOGIN = "Return";
    public static final int MAINSCREEN_REQUEST_CODE = 1;
    public static final int LOGIN_RETURN_RESULT_CODE = 1;
    public static final int LOGIN_SUCCESS_RESULT_CODE = 2;
    public static final int REGISTER_SUCCESS_RESULT_CODE = 3;
    public static final String INFO_LOGIN_SUCCESS_TO_MAINSCREEN_FROM_LOGIN = "LoginSuccess";
    public static final String INFO_REGISTER_SUCCESS_TO_MAINSCREEN_FROM_LOGIN = "RegisterSuccess";
    public static final boolean IS_OLD_USER = true;
    public static final boolean NOT_OLD_USER = false;
    public static final int TYPE_COLD_MEAL = 0;
    public static final int TYPE_HOT_MEAL = 1;
    public static final int TYPE_SEA_FOOD = 2;
    public static final int TYPE_LIQUOR = 3;
    public static final int ORDERED = 1;
    public static final int UNORDERED = 0;
    public static final int SUBMITTED = 1;
    public static final int NOT_SUBMITTED = 0;
    public static final int pageSubmittedMeals = 0;
    public static final int pageOrderedMeals = 1;
    public static final int ADD_ITEM = 1;
    public static final int DELETE_ITEM = 0;
    public static final int LOGIN_SUCCESS_STATE = 1;
    public static final int LOGIN_FAILED_STATE = 1;

    public static final int CALL_PHONE_PERMISSION_REQUEST_CODE = 1;
    public static final int SMS_PERMISSION_REQUEST_CODE = 2;
    public static final int INTERNET_PERMISSION_REQUEST_CODE = 3;

    public static final String HELP_PHONE_NUMBER = "5554";

    public static final String CALL_PHONE_PERMISSION_DENIED_INFO  = "拨号请求被拒绝";
    public static final String SEND_SMS_PERMISSION_DENIED_INFO  = "发送短信请求被拒绝";
    public static final String INTERNET_PERMISSION_DENIED_INFO = "使用网络请求被拒绝";
    public static final String SMS_CONTENT = "test scos helper";

    public static final int BIND_MSG = 2;
    public static final int START_UPDATE_FOODINFO = 1;
    public static final int STOP_UPDATE_FOODINFO = 0;
    public static final int STOCK_HAS_UPDATE = 10;
}
