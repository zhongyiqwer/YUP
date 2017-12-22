package cn.sharesdk.socilization;

import cn.sharesdk.socialization.SocializationCustomPlatform;
import android.content.Context;

import com.owo.utils.Common;
import com.owo.utils.util_http.MyURL;
import com.wao.dogcat.R;
/**
 * 自定义平台例子
 * <p>
 * 对于自己已经有用户系统的应用，希望可以利用自己系统里的用户
 *来完成评论功能，可以模范本类实现
 */
public class MyPlatform extends SocializationCustomPlatform {

	public MyPlatform(Context context) {
		super(context);
	}

	public String getName() {
		// 返回显示在帐号选择列表中的平台名称
		return "yup!";
	}

	public int getLogo() {
		// 返回显示在帐号选择列表中的平台图标
		return R.mipmap.ic_launcher;
	}

	protected boolean checkAuthorize(int action) {
		// 返回true，表示用户已经登录（或授权），ShareSDK可以执行操作
		// 返回false，表示用户没有登录（或授权），ShareSDK会中断操作，调用doAuthorize执行登录（或授权）
		// 因此，请添加登录（或授权）状态的判断，返回正确的结果，指导ShareSDK进行后续操作
		return true;
	}

	protected UserBrief doAuthorize() {
		// 引导用户完成登录（或授权）操作，然后返回UserBrief对象
		if (Common.user!=null) {
			UserBrief ub = new UserBrief();
			ub.userId = Common.userSP.getInt("ID", 0) + "";
			ub.userNickname = Common.user.getUserName();
			ub.userAvatar = MyURL.ROOT + Common.user.getAvatar();
			if (Common.userSP.getInt("sex", 0) == 1)
				ub.userGender = UserGender.Male;
			else if (Common.userSP.getInt("sex", 0) == 2)
				ub.userGender = UserGender.Female;
			else ub.userGender = UserGender.Unknown;
			ub.userVerifyType = UserVerifyType.Verified;
			return ub;
		}else return null;

	}

}
