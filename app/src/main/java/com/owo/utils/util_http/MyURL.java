package com.owo.utils.util_http;

import android.provider.DocumentsContract;

/**
 * Created by ppssyyy on 2016-08-05.
 */
public class MyURL {
    public static final String ROOT = "http://120.77.210.228:8080/loverapp/"; //NEW
    //public static final String ROOT = "http://139.199.174.189:8080/loverapp/";  //腾讯云
    //public static final String ROOT = "http://169.254.209.130:8081/loverapp/"; //本地
    // public static final String ROOT = "http://loswebcat.azurewebsites.net/loversAppwar/"; //微软

    /////////////version///////////////
    public static final String UPDATE_URL = ROOT + "Version/getVersion";

    ///////////////User///////////////

    public static final String UPDATE_BEHAVIOR = ROOT + "User/updateBehavior";

    public static final String GET_USER_BY_ID = ROOT + "User/getUserByID";
    public static final String GET_ID_BY_PHONE_AND_PW = ROOT + "User/getIDByPhoneAndPw";
    public static final String INSERT_USER = ROOT + "User/insertUser";
    public static final String UPDATE_PW_BY_ID = ROOT + "User/updatePwByID";
    public static final String UPDATE_AVATAR_BY_ID = ROOT + "User/updateAvatarByID";
    public static final String UPDATE_USERNAME_BY_ID = ROOT + "User/updateUserNameByID";
    public static final String GET_USER_BY_INVITE_CODE = ROOT + "User/getUserByInviteCode";
    public static final String UPDATE_SEX_BY_ID = ROOT + "User/updateSexByID";
    public static final String UPDATE_SIGNATURE_BY_ID = ROOT + "User/updateSignatureByID";
    public static final String UPDATE_STEPSTODAY_BY_ID = ROOT + "User/updateStepsTodayByID";
    public static final String UPDATE_LOCATION_BY_ID = ROOT + "User/updateLocationByID";
    public static final String UPDATE_WEXID_BY_ID = ROOT + "User/updateWexIDByID";
    public static final String UPDATE_QQ_BY_ID = ROOT + "User/updateQQByID";
    public static final String UPDATE_LEVEL_BY_ID = ROOT + "User/updateLevelByID";
    public static final String UPDATE_STATUS_BY_ID = ROOT + "User/updateStatusByID";
    public static final String UPDATE_CHECKINDAYS_BY_ID = ROOT + "User/updateCheckinDaysByID";
    public static final String UPDAET_AGE_BY_ID = ROOT + "User/updateAgeByID";
    public static final String UPDATE_HEIGHT_BY_ID = ROOT + "User/updateHeightByID";
    public static final String UPDATE_WEIGHT_BY_ID = ROOT + "User/updateWeightByID";
    public static final String UPDATE_HOBBY_BY_ID = ROOT + "User/updateHobbyByID";
    public static final String UPDATE_EXP_BY_ID = ROOT + "User/updateExpByID";
    public static final String UPDATE_BIMG_BY_ID = ROOT + "User/updateBimgByID";
    public static final String UPDATE_MONEY_BY_ID = ROOT + "User/updateMoneyByID";
    public static final String GET_INVITE_CODE_BY_ID = ROOT + "User/getInviteCodeByID";

    public static final String INSERT_FINISH_STATUS = ROOT + "User/insertFinishStatus";
    public static final String SET_FINISH_STATUS_BY_UID = ROOT + "User/setFinishStatusByUID";
    public static final String GET_FINISH_STATUS_BY_UID = ROOT + "User/getFinishStatusByUID";

    public static final String GET_USER_ORDER_BY_DIS = ROOT + "Extra/user/getUserOrderByDistance";
    public static final String GET_USER_ORDER_BY_MATCHINDEX = ROOT + "Extra/user/getUserOrderByMatchIndex";

    public static final String SEARCH_USER_BY_NAME = ROOT + "Extra/user/searchUserByName";
    public static final String INSERT_LOC = ROOT + "Extra/user/insertLocation";
    public static final String GET_LOC_BY_UID = ROOT + "Extra/user/getLocByUID";
    public static final String DELETE_LOC_BY_UID = ROOT + "Extra/user/deleteLocByUID";
    public static final String INSERT_SINGLE_SIGN = ROOT + "Extra/user/insertSingleSign";

    public static final String INSERT_STEPS = ROOT + "Extra/user/insertSteps";
    public static final String UPDATE_STEPS = ROOT + "Extra/user/updateSteps";
    public static final String GET_STEPS = ROOT + "Extra/user/getSteps";

    public static final String GET_INTERESTTAGBYSEX = ROOT + "User/getInterestTagBySex";
    public static final String GET_INTERESTTAG = ROOT + "User/getInterestTag";

    public static final String GET_USER_COMMENT_BY_UID = ROOT +"Extra/user/getUserCommentByUID";
    public static final String INSERT_USER_LABEL = ROOT +"Extra/user/insertUserLabel";
    public static final String INSERT_USER_COMMENT = ROOT +"Extra/user/insertUserComment";

    ///////////////friendShip///////////////
    public static final String INSERT_FRIEND = ROOT + "friendShip/insertFriend";
    public static final String CHECK_IS_FOLLOWED = ROOT + "friendShip/checkIsFollowed";
    public static final String DELETE_FRIEND = ROOT + "friendShip/deleteFriend";
    public static final String GET_FRIENDS_BY_UID = ROOT + "friendShip/getFriendsByUID";
    public static final String GET_FRIENDS_BY_FID = ROOT + "friendShip/getFriendsByFID";
    public static final String GET_SUMIFOLLOWED__BY_FID = ROOT + "friendShip/getMyFollowCount";
    public static final String GET_SUMFOLLOW_ME__BY_FID = ROOT + "friendShip/getFollowMeCount";


    ///////////////loverShip///////////////
    public static final String INSERT_LS = ROOT + "loverShip/insertLoverShip";
    public static final String GET_LID_BY_ID = ROOT + "loverShip/getloveIDByID";
    public static final String GET_LS_BY_ID = ROOT + "loverShip/getLoverShipByID";
    public static final String UPDATE_LINDEX_BY_ID = ROOT + "loverShip/updateStateByID";
    public static final String GET_HALF_BY_ID = ROOT + "loverShip/getHalfByID";
    public static final String GET_ALL_LS = ROOT + "loverShip/getAllLoveShips";
    public static final String UPDATE_LOVE_INDEX_BY_ID = ROOT + "loverShip/updateLoveIndexByID";
    public static final String UPDATE_STATE_BY_ID = ROOT + "loverShip/updateStateByID";
    public static final String CANCEL_LS = ROOT + "loverShip/cancelLoveShip";

    public static final String INSERT_LOVER_SIGNIN = ROOT + "loverShip/insertLoverSignin";
    public static final String BECOME_LOVER = ROOT + "loverShip/becomeLover";

    ///////////////ActivityRecords///////////////
    public static final String INSERT_RECORD = ROOT + "ActivityRecords/insertRecord";
    public static final String GET_ID_BY_LID = ROOT + "ActivityRecords/getIDByLoverID";
    public static final String GET_RECORD_BY_ID = ROOT + "ActivityRecords/getRecordByID";
    public static final String UPDATE_TOTAL_STEPS_BY_ID = ROOT + "ActivityRecords/updateTotalStepsByID";
    public static final String UPDATE_RADIUS_BY_ID = ROOT + "ActivityRecords/updateRadiusByID";
    public static final String UPDATE_LOCATION_BY_ID_AR = ROOT + "ActivityRecords/updateLocationByID";
    public static final String GET_ALL_RECORDS = ROOT + "ActivityRecords/getAllRecords";

    public static final String GET_PHOTOS_BY_UID = ROOT + "ActivityRecords/getPhotosByUserID";
    public static final String GET_PHOTOS_BY_RID = ROOT + "ActivityRecords/getPhotosByRecordsID";
    public static final String GET_TEXTS_BY_UID = ROOT + "ActivityRecords/getTextsByUserID";
    public static final String GET_TEXTS_BY_RID = ROOT + "ActivityRecords/getTextsByRecordsID";
    public static final String DELETE_TEXT_BY_ID = ROOT + "ActivityRecords/deleteTextByID";
    public static final String DELETE_PHOTO_BY_ID = ROOT + "ActivityRecords/deletePhotoByID";
    public static final String GET_TEXTS_EXCEPT_ONE = ROOT + "ActivityRecords/getTextsExceptOne";
    public static final String GET_PHOTOS_EXCEPT_ONE = ROOT + "ActivityRecords/getPhotosExceptOne";

    public static final String HAS_CIRCLE = ROOT + "ActivityRecords/hasCircle";

    ///////////////item///////////////
    public static final String GET_ALL_ITEMS = ROOT + "item/getAllItems";
    public static final String GET_ITEM_BY_ID = ROOT + "item/getItemByID";
    public static final String GET_ITEMS_BY_TYPE = ROOT + "item/getItemsByType";
    public static final String GET_ITEMS_BY_UID = ROOT + "item/getItemsByUID";
    public static final String INSERT_USER_ITEM = ROOT + "item/insertUserItem";

    public static final String DOODLE_PHOTO = ROOT + "item/doodlePhoto";
    public static final String INSERT_DOG = ROOT + "GoDie/insertDog";
    public static final String GET_DOG_HEAD_BY_ID = ROOT + "GoDie/getDogHeadByID";
    public static final String INSERT_CAPSULE = ROOT + "item/insertCapsule";
    public static final String SET_STATE_BY_ID = ROOT + "item/setStateByID";
    public static final String GET_CAPSULE_BY_CID = ROOT + "item/getCapsuleByCID";
    public static final String INSERT_GO_DIE = ROOT + "GoDie/insertGoDie";
    public static final String GET_DOG_ACT_BY_ID = ROOT + "GoDie/getDogActByID";
    public static final String DEL_GO_DIE_BY_ID = ROOT + "GoDie/deleteGoDieByID";
    public static final String INSERT_PHOTO = ROOT + "item/insertPhoto";
    public static final String INSERT_TXT = ROOT + "item/insertText";

    ///////////////TreaSure///////////////
    public static final String GET_TREASURE_BY_ID = ROOT + "TreaSure/getTreasureByID";
    public static final String INSERT_TREASURE = ROOT + "TreaSure/insertTreasure";
    public static final String GET_ALL_TREASURES = ROOT + "TreaSure/getAllTreasures";
    public static final String INSERT_DH = ROOT + "TreaSure/insertDigHistory";
    public static final String GET_DH_BY_UID = ROOT + "TreaSure/getDigHistoriesByUID";

    ///////////////message///////////////
    public static final String INSERT_MSG = ROOT + "message/insertMessage";
    public static final String GET_MSG_BY_RID = ROOT + "message/getMessageByRID";
    public static final String GET_MSG_BY_UID = ROOT + "message/getMessageByUID";
    public static final String DELETE_MSG_BY_ID = ROOT + "message/deleteMessageByID";

    public static final String SET_READ_BY_MID = ROOT + "message/setReadByMID";
    public static final String SET_ALL_READ_BY_RID = ROOT + "message/setAllReadByRID";
    public static final String GET_UNREAD_COUNT_BY_RID = ROOT + "message/getUnreadCountByRID";

    ///////////////Moment///////////////
    public static final String INSERT_MOMENT = ROOT + "Moment/insertMoment";
    public static final String DELETE_MOMENT_BY_ID = ROOT + "Moment/deleteMomentByID";
    public static final String GET_MOMENT_BY_UID = ROOT + "Moment/getMommentByUID";
    public static final String GET_MOMENT_BY_ID = ROOT + "Moment/getMommentByID";
    public static final String UPDATE_MOMENT_LIKE_BY_ID = ROOT + "Moment/updateLikeNumByMID";

    //根据commmentID查询有关点赞
    public static final String GET_MOMENT_LIKES_BY_COMMENT_ID = ROOT + "Moment/getLikesByCID";
    //根据commmentID查询有关reply
    public static final String GET_MOMENT_COMMENTS_BY_COMMENT_ID = ROOT + "Moment/getCommentByMID";


    ///////////////activity(task)///////////
    public static final String GET_TASK_I_APPLIED = ROOT + "task/getApplyTaskByUserID";//通过用户ID获取用户申请的所有任务
    public static final String GET_TASK_I_PUBLISHED = ROOT + "task/getTaskByUserID";//通过用户ID获取用户发布的所有任务
    public static final String GET_TASK_ORDER_BY_DIS = ROOT + "task/getAllTasksOrderByDistance";
    public static final String GET_TASK_RECOMEND = ROOT + "task/getRecommendTasks";
    public static final String INSERT_TASK = ROOT + "task/insertTask";
    public static final String DELETE_TASK_BY_ID = ROOT + "task/deleteTaskByID";
    public static final String INSERT_TASK_TOP = ROOT + "task/insertTaskTop";
    public static final String GET_TASK_BY_TAG = ROOT + "task/getTasksByTag";
    public static final String GET_TASK_BEFORE_DATE = ROOT + "task/getTasksBeforeDate";
    public static final String GET_APPLY_USERS_BY_TID = ROOT + "task/getApplyUserByTaskID";
    public static final String GET_TASK_BY_MAX_NUM = ROOT + "task/getTasksByTaskMaxNum";
    public static final String INSERT_APPLY = ROOT + "task/insertApply";
    public static final String UPDATE_TASK_STATUS_BY_ID = ROOT + "task/updateTaskStatusByID";
    public static final String UPDATE_APPLY_STATUS_BY_ID = ROOT + "task/updateApplyStatusByID";
    public static final String INSERT_TASK_COMMENT = ROOT + "task/insertTaskComment";
    public static final String GET_TASK_COMMENT_BY_TID = ROOT + "task/getTaskCommentByTID";
    public static final String DELETE_TASK_COMMENT_BY_CID = ROOT + "task/deleteTaskCommentByCID";
    public static final String GET_APPLY_STATUS_BY_UID_TID = ROOT + "task/getApplyStatusByUIDandTID";
    public static final String SET_APPLY_USERS_TASK_STATE = ROOT + "task/setApplyUsersTaskState";
    public static final String HAS_TASK_3 = ROOT + "task/hasTask3";
    public static final String SEARCH_TASK_BY_TASKNAME = ROOT + "task/searchTaskByTaskName";
    public static final String GET_TAKEN_USERS_BY_TASKID = ROOT + "task/getTakenUserByTaskID";
    public static final String ADD_TASK_TAKEN_NUM = ROOT + "task/addTaskTakenNum";
    public static final String UPDATE_TASK_TAKEN_NUM_BY_ID = ROOT + "task/updateTaskTakenNumByID";
    ///////////////百度地图///////////
    public  static  final  String BAIDU_MAP_URL =
            "http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&output=json&pois=1&ak=DyivwoFeCliVjmopXIgcqBrAMLHPdejA&mcode=9F:43:15:53:54:8B:B3:AC:9B:24:5F:2B:43:E3:E9:E8:6B:68:E7:B8;com.wao.dogcat";


}
