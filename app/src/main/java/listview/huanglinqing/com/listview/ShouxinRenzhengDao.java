package listview.huanglinqing.com.listview;

import android.content.Context;
import android.widget.SimpleAdapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShouxinRenzhengDao {


    public SimpleAdapter getAdapter(Context context){
       String titles[] = { "身份证认证", "基本信息认证","联系人认证","运营商认证","芝麻信用分","绑定银行卡" };
       int icon[] = {R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,
       R.mipmap.ic_launcher,R.mipmap.ic_launcher};
       List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
       SimpleAdapter adapter;
        for (int i = 0;i< titles.length ; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", titles[i]);
            map.put("icon",icon[i]);
            list.add(map);
        }
        adapter = new SimpleAdapter(context, list, R.layout.shouxinrenzhengliebiao,
                new String[] { "title", "icon"}, new int[] { R.id.title,
                R.id.image });
        return adapter;
    }
}
