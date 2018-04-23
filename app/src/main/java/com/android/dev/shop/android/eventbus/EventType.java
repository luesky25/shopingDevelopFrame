package com.android.dev.shop.android.eventbus;

public class EventType {
//
//	/** 通知activity进行finish操作 **/
//	public static final int EVENT_FINISH_ACTIVITY = 0x001;
//
	/** test 信鸽应用内信息操作 **/
	public static final int EVENT_XINGE_MSG_FROM_APP = 0x001;
	/** test 信鸽应用外通知栏信息操作 **/
	public static final int EVENT_XINGE_MSG_NOTI_FROM_APP = 0x002;
	
	

	/**
	 * 用户输入手机 验证码之后 然后更新数据
	 */
	public static final int EVENT_RINGTONE_POHNE_LOGIN_SUCCESS = 0x004;


	/**
	 * 用户输入手机 验证码之后 然后更新数据
	 */
	public static final int EVENT_RINGTONE_POHNE_DELETED_SUCCESS = 0x005;


	/**
	 * 用户收到短信校验码
	 */
	public  static  final int EVENT_SHOPPING_SMS_VERIFY_SUCCESS = 0x006;

	public  static  final int EVENT_RINGTONE_SEARCHE_TEXT_TO_REQUEST = 0x007;

	/**
	 * 切换Sim卡
	 */
	public static final int EVENT_RINGTONE_SWITCH_SIM_CARD = 0x008;

	/**
	 * 响应Sim卡状态改变
	 */
	public static final int EVENT_RINGTONE_SIM_CARD_CHANGED = 0x009;


	/**
	 * 我的管理页面需要刷新（双卡双待的卡数量发生变化）
	 */
	public static final int EVENT_RINGTONE_SIM_CARDS_MANAGE_PAGE_UPDATE = 0x010;

	/**
	 * 试听状态
	 */
	public static final int EVENT_RINGTONE_RING_TONG_UPDATE_STATE = 0x011;

	/**
	 * 移动sdk初始化
	 */
	public static final int EVENT_SDK_CMM_INIT = 0x012;
	/**
	 * 更新用户信息
	 */
	public static final int EVENT_USER_UPDATE_INFO = 0x013;

	/**
	 * 登录之后更新主页
	 */
	public static final int EVENT_USER_LOGIN_UPDATE_INFO = 0x014;

	/**
	 * 刷新闹钟铃声
	 */
	public static final int EVENT_UODATE_RING = 0x015;

	/** session过期，通知mainActivity跳转登录页面  **/
	public static final int EVENT_SESSION_FAIL_TO_LOGIN = 0x016;

	/** 留言板点回复通知最外界面弹出软键盘  **/
	public static final int EVENT_MESSAGE_EDITTEXT = 0x018;

	/**
	 * 刷新个人编辑页主页背景
	 */
	public static final int EVENT_UODATE_USER_BACKGROUND = 0x017;

	/**
	 * 导量弹框
	 */
	public static final int EVENT_SHOW_DIALOG = 0x019;

	/**
	 * 提现后刷新我的财富页
	 */
	public static final int EVENT_UPDATE_MY_WEALTH = 0x020;

	/**
	 * 铃声库上传铃声之后 刷新上传状态
	 */
	public static final int EVENT_UPDATE_RINGTONE_UPLOAD = 0x021;

	/**
	 * 由于没有写配置权限导致设置铃声错误
	 */
	public static final int EVENT_SETTING_ERR_NO_WRITE_SETTING_PERMISSION = 0x022;

	/**
	 * 红点显示
	 */
	public static final int EVENT_SHOW_RED_TIP_NOTIFY = 0x023;
	/**
	 * 取消红点显示
	 */
	public static final int EVENT_SHOW_CACEL_RED_TIP_NOTIFY = 0x024;

	/**
	 * 微信充值刷新页面
	 */
	public static final int EVENT_UPDATE_MYWEALTHACTIVITY_DATE = 0x025;

	/**
	 * 暂停音乐
	 */
	public static final int EVENT_SETTING_STOP_MUSIC = 0x0118;

	/**
	 * 关闭锁屏
	 */
	public static final int EVENT_SETTING_CLOSE_LOCKSCREEN = 0x0119;

	/**
	 * 更新用户信息
	 */
	public static final int EVENT_PASSWORD_UPDATE_LOGIN_INFO = 0x026;

	/**
	 * 假网通知
	 */
	public static final int EVENT_NETWORK_EXCEPTION = 0x027;


	/**
//	 *消息中心小红点 (KTV+消息数)
	 */
	public static final int EVENT_MSG_NUMBER_RED= 0x028;

	/**
	 *系统消息推送 用在消息中心 取消红点
	 */
	public static final int EVENT_MSG_SYSTEM_MESSAGE= 0x029;

	/**
	 *消息中心小红点 在ktv 里也有用到 请忽改 0x030 是固定的
	 */
	public static final int EVENT_MSG_KTV_NUMBER_RED= 0x030;
	/**
	 *消息中心小红点 在ktv点击 之后通知我的页，算被看的ktv数量 里也有用到 请忽改 0x031 是固定的
	 */
	public static final int EVENT_MSG_KTV_MANAGER_NUMBER_RED= 0x031;

	/**
	 *首页tab我的消息中心小红点 (KTV+消息数+朋友数)
	 */
	public static final int EVENT_MSG_ALL_MESSAGE_NUMBER_RED= 0x032;

	/**
	 *我的朋友红点
	 */
	public static final int EVENT_MSG_CONTACT_FRIEND_RED= 0x033;
	/**
	 *我的朋友取消的红点
	 */
	public static final int EVENT_MSG_CONTACT_FRIEND_RED_CANCAL= 0x034;


	/**
	 * K歌作品上传之后 刷新上传状态
	 */
	public static final int EVENT_UPDATE_K_SONG_UPLOAD = 0x035;


	/**
	 * ktv 聊天信息刷新
	 */
	public static final int EVENT_KTV_CHART_UPDATE = 0x036;

	/**
	 * 铃声播放告诉ktv的歌神榜 请勿改值
	 */
	public static final int EVENT_KTV_STOP_RING = 0x037;

	/**
	 *绑定手机号之后UI修改
	 */
	public static final int EVENT_PHONE_BOUND = 0x038;
}