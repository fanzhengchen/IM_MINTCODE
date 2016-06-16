package mintcode.com.workhub_im.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mintcode.com.workhub_im.R;
import mintcode.com.workhub_im.daohelper.SessionItemDaoHelper;
import mintcode.com.workhub_im.db.SessionItem;

/**
 * Created by mark on 16-6-14.
 */
public class MessageFragment extends Fragment {
    private List<SessionItem> sessionItems = null;
    private SessionItemDaoHelper daoHelper = null;
    private View rootView;

    @BindView(R.id.session_recycler_view)
    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        daoHelper = SessionItemDaoHelper.getInstance();
        sessionItems = daoHelper.getListByDesc();
//        daoHelper.deleteAll();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


}
