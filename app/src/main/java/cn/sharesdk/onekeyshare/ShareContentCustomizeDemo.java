package cn.sharesdk.onekeyshare;

/**
 * Created by ppssyyy on 2017-04-06.
 */

import cn.sharesdk.framework.Platform;

/**
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

import com.wao.dogcat.R;

import cn.sharesdk.framework.Platform;

        import cn.sharesdk.framework.Platform;
        import cn.sharesdk.framework.Platform.ShareParams;
        import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;


public class ShareContentCustomizeDemo implements ShareContentCustomizeCallback {
    private String inviteCode;

    public ShareContentCustomizeDemo(String inviteCode){
        this.inviteCode = inviteCode;

    }

    public void onShare(Platform platform, ShareParams paramsToShare) {

                //platform.getContext().getString(R.string.share_title);
        if ("WechatMoments".equals(platform.getName())) {
            // 改写twitter分享内容中的text字段，否则会超长，
            // 因为twitter会将图片地址当作文本的一部分去计算长度
           // text += platform.getContext().getString(R.string.share_to_wechatmoment);
            //paramsToShare.setText(text);
        }else if("SinaWeibo".equals(platform.getName())){
           // text +=" SinaWeibo";
           // paramsToShare.setText(text);
        }else if("TencentWeibo".equals(platform.getName())){
           // text += " TencentWeibo";
           // paramsToShare.setText(text);
        }else if("ShortMessage".equals(platform.getName())){
           // text += " ShortMessage";
            paramsToShare.setText("我邀请你一起使用情侣应用OWO，快来下载和我一起玩吧~ 邀请码："+inviteCode);
        }
    }

}