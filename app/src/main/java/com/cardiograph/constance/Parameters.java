package com.cardiograph.constance;

import java.util.ArrayList;
import java.util.List;

import com.cardiograph.model.User;

/**
 * 全局参数类,提供全局生命周期的静态数据
 * 
 * @author 金同宝
 * 
 */
public class Parameters {
	private Parameters() {

	}

//	public static ShoudanRegisterInfo merchantInfo = null;
	
	public static String installationId="";

	/**
	 * 商户名
	 */
	public static String merchantName = "";
	/** 渠道包号 */
	public static final String CHANNEL_FLAG_AnZhi = "2";// 2号包为安智市场
	/**
	 * 商户号
	 */
	public static String merchantNo = "";

	public static String rentInpan = "";

//	public static List<TransactionManager.TransType> chargeBusinesses = new ArrayList<TransactionManager.TransType>();
	/** 应用图标配置文件 **/
	public final static String appIconConfig = "appicon.config";

	/**
	 * 
	 * 商户状态
	 * 
	 * 商户状态,0:未开通;1:已开通;2:冻结;3:审核未通过; 4,修改过注册信息并且通过审核
	 * 
	 */
	public static int merchantState = 0;

	/**
	 * 
	 * 用户状态
	 * 
	 * true未通过,可以被修改(null，“REJECT”，“NONE" 可以提交) false通过不可被修改( PASS，APPLY不能提交)
	 * 
	 */
	public static boolean authState = true;

	public static boolean isLphone = false;

	public static int connectType = 0;

	/**
	 * 是否放弃匹配刷卡器 true 放弃绑定刷卡器，关闭当前activity
	 */
	public static boolean isGiveUp = false;

	/**
	 * 接入地址类型
	 */
	public enum UrlType {
		TEST_INTRANET, // 测试内网
		TEST_INTERNET, // 测试外网
		PRODUCT, // 生产
		RESERVE, // 备机
	}

	/**
	 * 接入地址类型
	 */
	public static UrlType urlType = UrlType.TEST_INTERNET;

	/**
	 * 调试标记，正式版必须置为 false 。 开启调试后，所有的http请求均会添加 debug=1 参数。
	 * 控制是否做签名验证，为true时，不进行签名验证；为false，且当版本号大于等于13时，进行签名验证
	 */
	public static final boolean debug = true;
	/**
	 * 下载渠道标识,发布的时候需要根据不同的渠道修改此值
	 */
//	public static final String downloadFlag = Util.getChanel();
	public static final int uploadDelayMinutes = debug ? 1 : 10;

	/**
	 * 生产&测试服务器地址开关 ，true：使用测试服务器， false：使用生产服务器
	 */
	public static boolean useDeveloperURL = true;

	/**
	 * 是否使用生产内网务器，true 使用(生产备机)，false 使用外网服务器(正式的服务器)。
	 */
	public static final boolean userLanServer = true;

	/**
	 * 在生个http请求被应答后，是否检测UserToken有效。
	 */
	public static boolean httpResponseAfterCheckToken = true;

	/** 主服务器地址，该地址在 initServerAddress() 中初始化。 */
	public static String serviceURL = "";

	/** 便利特惠服务地址，该地址在 initServerAddress() 中初始化。 */
	public static String bianlitehuiServiceUrl = "";

	/** 数据统计相关地址，该地址在 initServerAddress() 中初始化。 */
	public static String statisticsURL = "";

	public final static String TAG = "lakala_tag";
	public final static String BUY_SHUAKAQI_RUL = "http://m.lakala.com/quote/buy_skb.html";
	public final static String WEIBO_URL = "http://www.weibo.cn/lakala";
	public final static String WINXINLOADDOWN = "http://weixin.qq.com/d";
	/** 钱包充值页面充值说明页的地址 */
	public final static String WALLET_RECHARGE_URL = useDeveloperURL ? "http://10.1.21.63:8080/film_ticket/czsm.jsp?platType=1" : "http://user.lakala.com/film_ticket/czsm.jsp?platType=1";

	/** 帮助页面前缀 */
	private final static String HELP_URL_PREFIX = "http://download.lakala.com.cn/lklmbl/html/";

	/** 更多帮助页面的地址 */
	public final static String MORE_HELP_PAGE_URL = HELP_URL_PREFIX + "help.html";

	/** 电影票购票帮助页的地址 */
	public final static String MOVIE_BUY_TICKET_HELP_URL = HELP_URL_PREFIX + "movie_help.html";

	/** 大额转账协议页的地址 */
	public final static String BIG_AMOUNT_REMIT_PROTOCAL_URL = HELP_URL_PREFIX + "largetransfers.html";

	/** 大额开通说明页的地址 */
	public final static String BIG_AMOUNT_REMIT_DESCRIPTION_URL = HELP_URL_PREFIX + "open_instruction.html";

	/** 红包规则 */
	public final static String BONUS = HELP_URL_PREFIX + "bonus.html";

	/** 钱包服务协议 */
	public final static String WALLET_SERVICE = HELP_URL_PREFIX + "wallet_service.html";

	/** 提现收费标准 */
	public final static String TIXIAN_GUI_ZHE = HELP_URL_PREFIX + "the_charging_standard.html";

	/** 无卡还款服务协议 */
	public final static String NO_CARD_SERVICE = HELP_URL_PREFIX + "no_card_service.html";

	/** 手机号汇款服务协议 */
	public final static String MOBILE_REMITTANCE_SERVICE = HELP_URL_PREFIX + "mobile_transfer.html";

	/** 手机号汇款更多说明 */
	public final static String MOBILE_REMITTANCE_MORE_SERVICE = HELP_URL_PREFIX + "mobile_more.html";

	/** 开通大额转账的收费标准的页面 */
	public final static String ZHUAN_ZHANG_FEE_URL = HELP_URL_PREFIX + "fee_scale.html";

	/** 开通大额转账的收费标准的页面 */
	public final static String SHOU_KUAN_BAO_URL = HELP_URL_PREFIX + "skb_help.html";

	/** 收款宝使用服务协议 */
	public final static String SHOU_KUAN_BAO_SERVICE_URL = HELP_URL_PREFIX + "skb_service.html";

	/** 收款宝费率 */
	public final static String SHOU_KUAN_BAO_RATE_URL = HELP_URL_PREFIX + "skb_rate.html";

	/** 收款宝交易规则 */
	public final static String TRANSACTION_RULES_OF_SHOUDAN = HELP_URL_PREFIX + "skb_rate.html";

	/** 电子签名说明 */
	public final static String ELECTRONIC_SIGNATRUE_HELP = HELP_URL_PREFIX + "skb_sign_help.html";

	/** 撤销帮助 */
	public final static String COLLECTION_CANCEL_HELP = HELP_URL_PREFIX + "skb_abolish_help.html";

	/** 升级服务说明 **/
	public final static String UPGRADE_HELP = HELP_URL_PREFIX + "skb_update.html";
	/**
	 * 关注拉卡拉
	 */
	public final static String FOLLOW_LAKALA = HELP_URL_PREFIX + "skb_lklwx.html";

	/**
	 * 特约缴费说明
	 */
	public final static String TEYUEJIAOFEI_DESCRIPTION = HELP_URL_PREFIX + "skb_teyue.html";

	/**
	 * 特约缴费协议
	 */
	public final static String TEYUEJIAOFEI_PRO = HELP_URL_PREFIX + "skb_teyuexieyi.html";

	/** 连接帮助 **/
	public final static String CONNECTION_HELP = HELP_URL_PREFIX + "skb_connect.html";
	/**
	 * 用户服务协议
	 */
	public final static String LAKALA_USER = HELP_URL_PREFIX + "lkl_user.html";

	/**
	 * 帮助
	 */
	public final static String HELP_SERVICE_URL = HELP_URL_PREFIX+"hebao_help.html";
	
	/**
	 * 关于
	 */
	public final static String ABOUT_SERVICE_URL = HELP_URL_PREFIX+"hebao_about.html";

	// 新增帮助，使用本地文件
	public final static String LOCAL_HELP_URL = "http://download.lakala.com.cn/lklmbl/newhtml/";
	/**
	 * 商户收款帮助8
	 */
	public final static String LOCAL_HELP_MERCHANT_COLLECTION = LOCAL_HELP_URL + "skb_help_shoukuan.html";
	/**
	 * 撤销交易帮助2
	 */
	public final static String LOCAL_HELP_CANCEL_TRANS = LOCAL_HELP_URL + "skb_help_chexiao.html";
	/**
	 * 信用卡还款帮助3
	 */
	public final static String LOCAL_HELP_HUANKUAN = LOCAL_HELP_URL + "skb_help_huankuan.html";
	/**
	 * 转账汇款帮助10
	 */
	public final static String LOCAL_HELP_TRANSFER = LOCAL_HELP_URL + "skb_help_zhuanzhang.html";
	/**
	 * 产品适配帮助6
	 */
	public final static String LOCAL_HELP_PRODUCTADAPTER = LOCAL_HELP_URL + "skb_help_shipei.html";
	/**
	 * 产品连接帮助4
	 */
	public final static String LOCAL_HELP_PRODUCTCON = LOCAL_HELP_URL + "skb_help_lianjie.html";

	/**
	 * 注册开通帮助11
	 */
	public final static String LOCAL_HELP_REGISTER_OPEN = LOCAL_HELP_URL + "skb_help_zhuce.html";
	/**
	 * 关于刷卡帮助9
	 */
	public final static String LOCAL_HELP_ABOUT_SWIP = LOCAL_HELP_URL + "skb_help_shuaka.html";
	/**
	 * 交易安全1
	 */
	public final static String LOCAL_HELP_TRANSSAVE = LOCAL_HELP_URL + "skb_help_anquan.html";
	/**
	 * 资料审核和修改12
	 */
	public final static String LOCAL_HELP_INFO_UPDATE = LOCAL_HELP_URL + "skb_help_ziliao.html";
	/**
	 * 商户升级5
	 */
	public final static String LOCAL_HELP_MERCHANT_UP = LOCAL_HELP_URL + "skb_help_shengji.html";
	/**
	 * 产品售后7
	 */
	public final static String LOCAL_HELP_PRODUCT_SERVICE = LOCAL_HELP_URL + "skb_help_shouhou.html";

	public final static String NIGHT_HELP = LOCAL_HELP_URL + "skb_help_bak.html";

	public static final String DESCRIPTION = "http://download.lakala.com.cn/lklmbl/newhtml/shengji/";
	public final static String DES_REMITTANCE_OPEN_APPLY = DESCRIPTION + "zhaohangykt.html";
	public final static String DES_MERCHANT_UP_LEVEL = DESCRIPTION + "up.html";

	public static final String SETTLEMENT_MERCHANT_CHANGE = DESCRIPTION + "zhaohang.html";
	
	/** 应用程序数据存储路径 */
//	public static String appDataPath = "/data/data/" + ApplicationExtension.getInstance().getPackageName();

	/** 引导页开始进入主界面button距离底部基础距离（以此为基础按分辨率来进行计算实际距离） **/
	public static int marginToBottom = 115;

	/** 保存前一次插入的刷卡头的串号 **/
	public static String preSwiperNo = "";

	/** 前一次插入刷卡头的状态 **/
//	public static ESwiperState preSwiperState = ESwiperState.unusable;

	/** 前一次插入刷卡头对应的用户名 **/
	public static String preUserForSwiper = "";

	/** 当前刷卡头的串号 **/
	public static String swiperNo = "";

	/** 当前刷卡头是否开通 **/
//	public static ESwiperState swiperState = ESwiperState.unusable;

	/** 当前刷卡头对应的用户 **/
	public static String userForSwiper = "";

	public static String serverTel = "";

	/** 屏幕分辨率,主要分为高中低 */
//	public static EScreenDensity screenDensity = EScreenDensity.MDPI;

	public static int densityDpi = 240;

	/** 屏幕宽度 **/
	public static int screenWidth;

	/** 屏幕高度 **/
	public static int screenHeight;
	/** 缓存便利特惠商品ID 便于PV统计 */
	public static int productId = -1;
	/** 购买刷卡器方式 */
	public static int GouMai_Type = -1;

	/** 数据统计缓存商品的Pid */
	public static int pid = -1;
	/** 数据统计缓存商品pCode */
	public static int pCode = -1;
	/** Dialog停留的时间 */
	public static int dialogResidenceTime = 2000;

	/** 网络请求成功后返回的状态码 */
	public static final String successRetCode = "0000";
	/** 网络问题,重新做 */
	public static final String networkProblem = "0009";
	/** 验证码验证失败 */
	public static final String vercodeError = "1001";
	/** 用户不存在 */
	public static final String userNoExists = "1002";
	/** 用户已经存在 */
	public static final String userHasExists = "1003";
	/** 用户名或密码错误 */
	public static final String userOrPWError = "1004";
	/** 请求数据为空 */
	public static final String reqDataNull = "1007";
	/** 系统异常 */
	public static final String sysAbnormal = "1010";
	/** 密码错误 */
	public static final String pwError = "1013";
	/** 无效的Token (距上次登录时间过长，请重新登录)*/
	public static final String tokenOutOfDate = "1023";
	// 获取账单详情列表为空
	public static final String tradeRecordNull = "1027";
	// 交易超时统一code
	public static final String tranTimeOut = "1030";
	// 超出每日短信限额
	public static final String msgNoSendAmount = "1032";
	// 中银通圈存不支持信用卡
	public static final String creditCardNotSupport = "00Re";
	// 暂不支持或受理您的卡
	public static final String cardNotSupport = "00RT";
	/** Android 客户端 ID */
	 public static final String androidClientID = "HHHwsaAnroid_sdfa@a22222sfasd1$%^&&882**(2";
//	public static final String androidClientID = "asdfas@weeweaPos@saqwqwqqw228228()#44%";

	// 调试环境传输保护密钥
	public static String debugTransferKey = "11112222333344445555666677778888";

	public static User user = new User();

	public final static String PACKAGENAME = "com.chinamobile.schebao.lakala";
	// 下载服务发送广播:开始下载
	public final static String DOWNLOAD_SERVICE_DOWNLOAD_START_ACTION = PACKAGENAME.concat(".broadcast.download.start");
	// 下载服务发送广播：更新进度条
	public final static String DOWNLOAD_SERVICE_DOWNLOAD_PROGRESS_ACTION = PACKAGENAME.concat(".broadcast.download.progress");
	// 下载服务发送广播:下载已经完成
	public final static String DOWNLOAD_SERVICE_DOWNLOAD_COMPLETE_ACTION = PACKAGENAME.concat(".broadcast.download.complete");
	// 下载服务发送广播:断网的状态
	public final static String DOWNLOAD_SERVICE_DISCONNECT_NETWORK_ACTION = PACKAGENAME.concat(".broadcast.download.error");
	// 下载服务发送广播:取消下载
	public final static String DWONLOAD_SERVICE_DOWNLOAD_CANCEL_ACTION = PACKAGENAME.concat(".broadcast.download.cancel");
	/** 推送消息轮询时间 */
	public final static int pushMessagegTime = debug ? 5 : 60;

	/** 图片缓存路径 **/
	public final static String imageCachePath = "/lakala/imgCache/";

	/**
	 * 初始化服务器地址
	 */
	public static void initServerAddress(){
        switch (urlType){
            case PRODUCT:
//              serviceURL = "https://mpos.lakala.com/apos/";
//                serviceURL = "https://mobile.lakala.com/android/";
                serviceURL = "https://mobile.lakala.com/HBA/";
                useDeveloperURL = false;
                break;
                
            case RESERVE:
                useDeveloperURL = false;
                serviceURL = "https://mobile.lakala.com:6443/apos/";
                break;
                
            case TEST_INTERNET:
                useDeveloperURL = true;
//                serviceURL = "http://180.166.12.107:8280/mpos/apos/";
                serviceURL = "http://1.202.150.4:8880/lakalaukeyRest/android/";
                break;
                
            case TEST_INTRANET:
                useDeveloperURL = true;
//                serviceURL = "http://10.7.111.32:8280/mpos/apos/";
                serviceURL = "http://10.5.12.66:8080/lakalaukeyRest/android/";
                break;
                
            default:
                break;
        }
	}

	public static void clear() {
//		Parameters.user.clear();
//		Parameters.userForSwiper = "";
//		Parameters.swiperNo = "";
//		Parameters.merchantName = "";
//		Parameters.merchantNo = "";
//		Parameters.merchantState = 0;
//		Parameters.authState = false;
//		Parameters.merchantInfo = null;
//		Parameters.rentInpan = "";
//		chargeBusinesses.clear();
//		DBManager.getInstance().destroy();
//		TerminalKey.clear();
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//
//				SwiperManager swiperManeger = SwiperManager.getInstance();
//				swiperManeger.setIsSwiperValid(false);
//				swiperManeger.deleteSwiper();
//			}
//		}).start();
	}

}
