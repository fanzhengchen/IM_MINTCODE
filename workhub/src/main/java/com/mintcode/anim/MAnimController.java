package com.mintcode.anim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

/**动画控制视图类。<br/>
 * 使用方法如下：<br/>
 * <code>
 * MAnimController test = new MAnimController(animaiton);<br/>
 * test.play(view, View.GONE);<br/>
 * </code>
 * play这个方法就是传入视图和播完动画后的视图的可视状态。
 * @author RobinLin
 *
 */
public class MAnimController implements AnimationListener{
	private Animation mAnimation;
	private View mView;
	private int mResultVisible;
	public MAnimController(Animation anim){
		initAnim(anim);
	}
	
	public void setAnimation(Animation anim){
		initAnim(anim);
	}
	
	private void initAnim(Animation anim){
		mAnimation = anim;
		mAnimation.setAnimationListener(this);
	}
	
	/**播放动画
	 * @param view 传入的view
	 * @param resultVisible 播放完毕动画后的可见状态。
	 */
	public void play(View view,int resultVisible){
		if(view.getVisibility() == resultVisible){
			return;
		}
		if(view.getVisibility() != View.VISIBLE){
			view.setVisibility(View.VISIBLE);
		}
		mResultVisible = resultVisible;
		if(mAnimation.hasStarted() || mAnimation.hasEnded()){
			mAnimation.cancel();
		}
		mView = view;
		view.startAnimation(mAnimation);
	}

	@Override
	public void onAnimationStart(Animation animation) {
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		//动画播放完毕，设置可视状态。
		mView.setVisibility(mResultVisible);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}
}
