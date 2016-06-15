package com.mintcode.chat.activity;

import android.os.Bundle;
import android.text.SpannableString;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.mintcode.chat.emoji.EmojiParser;
import com.mintcode.chat.emoji.ParseEmojiMsgUtil;
import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;

public class TextActivtiy extends BaseActivity {

	public static final String KEY = "big_size_text";
	private TextView mTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(0, 0);
		setContentView(R.layout.activity_chat_big_text);
		mTextView = (TextView) findViewById(R.id.textview);
		String text = getIntent().getStringExtra(KEY);
		String unicode = EmojiParser.getInstance(this).parseEmoji(text);
		SpannableString spannableString = ParseEmojiMsgUtil
				.getExpressionString(this, unicode);
		mTextView.setText(spannableString);
		mTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}


	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(0, 0);
	}

}
