package com.qin.wsp;

//import com.zgl.testgis.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.wsplog.app1.R;
public class GuanggaoViewFragment extends Fragment {
private int count;
private ImageView imageview;
GuanggaoViewFragment(int count) 
{
	this.count=count;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View fLayout = inflater.inflate(R.layout.guanggaoimage, container, false);
		imageview=(ImageView)fLayout.findViewById(R.id.ggimage);
		if(this.count==1)
			{imageview.setImageResource(R.drawable.ic_launcher);}//();
		else if(this.count==2)
		{
			imageview.setImageResource(R.drawable.ic_launcher);
		}
		else if(this.count==3)
		{
			imageview.setImageResource(R.drawable.ic_launcher);
		}
		return fLayout;
	}

}
