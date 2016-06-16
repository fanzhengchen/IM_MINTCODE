package mintcode.com.workhub_im.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.fragments.ContactsFragment;
import mintcode.com.workhub_im.fragments.MessageFragment;

public class MainActivity extends FragmentActivity implements TabHost.OnTabChangeListener {

    @BindView(android.R.id.tabhost)
    FragmentTabHost mTabHost;

    private int curTabIdx = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTabHost();
        setTabSelected(0, true);
    }

    private void initTabHost() {
        mTabHost.setOnTabChangedListener(this);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.real_tab_content);
        for (int i = 0; i < tabs.size(); ++i) {
            final TabDesc td = tabs.get(i);
            final View tabView = makeTabView();
            final TextView textView = (TextView) tabView.findViewById(R.id.tab_label);
            textView.setText(td.name);
            mTabHost.addTab(mTabHost.newTabSpec(td.tag).setIndicator(tabView), td.frgClass, null);
        }

    }

    private static final ArrayList<TabDesc> tabs = new ArrayList<TabDesc>() {
        {

            add(TabDesc.create("LastMsg", R.string.message, R.drawable.icon_message,
                    R.drawable.icon_message_checked, MessageFragment.class));
            add(TabDesc.create("Contacts", R.string.contacts, R.drawable.icon_contact,
                    R.drawable.icon_contact_checked, ContactsFragment.class));
        }
    };

    private void refreshTab(View view, TabDesc td, boolean isSelected) {
        final TextView textView = (TextView) view.findViewById(R.id.tab_label);
        final ImageView imageView = (ImageView) view.findViewById(R.id.tab_image);

        if (isSelected) {
            imageView.setImageResource(td.isSelected);
            textView.setTextColor(ContextCompat.getColor(this, R.color.blue));
        } else {
            imageView.setImageResource(td.unSelected);
            textView.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
        }
    }

    private void setTabSelected(int tabIdx, boolean selected) {
        refreshTab(mTabHost.getTabWidget().getChildTabViewAt(tabIdx), tabs.get(tabIdx), selected);
    }

    @Override
    public void onTabChanged(String tabId) {
        int idx = mTabHost.getCurrentTab();
        if (curTabIdx == idx) {
            return;
        }
        setTabSelected(curTabIdx, false);
        curTabIdx = idx;
        setTabSelected(curTabIdx, true);
    }

    private static class TabDesc {
        String tag;
        int name;
        int unSelected;
        int isSelected;
        Class<? extends Fragment> frgClass;

        static TabDesc create(String tag, int name, int unSelected, int isSelected, Class<? extends Fragment> frgClass) {
            TabDesc tabDesc = new TabDesc();
            tabDesc.tag = tag;
            tabDesc.name = name;
            tabDesc.unSelected = unSelected;
            tabDesc.isSelected = isSelected;
            tabDesc.frgClass = frgClass;
            return tabDesc;
        }
    }

    private View makeTabView() {
        View view = getLayoutInflater().inflate(R.layout.tab_view, mTabHost.getTabWidget(), false);
        return view;
    }

}
