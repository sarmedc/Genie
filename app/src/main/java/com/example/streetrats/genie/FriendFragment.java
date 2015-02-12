package com.example.streetrats.genie;

        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ListAdapter;
        import android.widget.ListView;


public class FriendFragment extends Fragment {

    private final String[] items = {"Xbox One", "Macbook Pro", "GTA 5", "Sweater", "Sofa", "Protein Powder",
            "Lightbulb", "Backpack", "Canteen", "Toy", "Desktop"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_browsefriends,
                container, false);

        ListAdapter theAdapter = new MyAdapter(getActivity(), items);

        ListView theListView = (ListView) view.findViewById(R.id.listView);

        theListView.setAdapter(theAdapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_browsefriends, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*switch (item.getItemId()) {
            case R.id.action_settings:
                break;
        }*/
        return true;
    }
}

