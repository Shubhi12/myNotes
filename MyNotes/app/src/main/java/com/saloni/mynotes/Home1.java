package com.saloni.mynotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static android.R.attr.data;
import static android.R.attr.mode;
import static com.saloni.mynotes.R.layout.activity_list_item;
import static com.saloni.mynotes.R.layout.fragment_home1;


public class Home1 extends Fragment {
    private static final String TAG = "Home1";
    private NDb mydb;
    RecyclerView mylist;
    private EntityAdapter adapter;
    FloatingActionButton btnadd;
    //private SelectionAdapter mAdapter;
    Entity e;
    ArrayList<Entity> al = new ArrayList<>();
    ArrayList<Entity> multiselect_list=new ArrayList<>();
    ArrayList<Integer> pos = new ArrayList<>();
    SparseBooleanArray mSelectedItemsIds;
    boolean isMultiSelect = false;
    Menu context_menu;
    android.view.ActionMode mActionMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        final View rootview = inflater.inflate(R.layout.fragment_home1, container, false);
        mylist = (RecyclerView) rootview.findViewById(R.id.recycler_view);
        btnadd = (FloatingActionButton) rootview.findViewById(R.id.btnadd);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", 0);
                Intent intent = new Intent(getActivity(),
                        EditNote.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
                getActivity().finish();
            }
        });
        //alertDialogHelper =new AlertDialogHelper(getActivity());
        mydb = new NDb(getActivity());
        al = mydb.getAll();
        adapter = new EntityAdapter(getActivity(), al, multiselect_list);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        mylist.setLayoutManager(mLayoutManager);
        mylist.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(5), true));
        mylist.setItemAnimator(new DefaultItemAnimator());
        mylist.setAdapter(adapter);
        mylist.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), mylist, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (isMultiSelect) {
                            multi_select(position);
                        }
                        else {
                            CardView cv = (CardView) view;
                            LinearLayout linearLayout = (LinearLayout) cv.getChildAt(0);
                            LinearLayout child = (LinearLayout) linearLayout.getChildAt(0);
                            TextView id = (TextView) child.getChildAt(1);
                            Bundle dataBundle = new Bundle();
                            dataBundle.putInt("id",
                                    Integer.parseInt(id.getText().toString()));
                            Intent intent = new Intent(getActivity(),
                                    EditNote.class);
                            intent.putExtras(dataBundle);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, final int position) {
                        if (!isMultiSelect) {
                            multiselect_list = new ArrayList<Entity>();
                            isMultiSelect = true;

                            if (mActionMode == null)
                                mActionMode =getActivity().startActionMode(new android.view.ActionMode.Callback() {
                                    @Override
                                    public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                                        MenuInflater inflater = mode.getMenuInflater();
                                        inflater.inflate(R.menu.actionbar, menu);
                                        context_menu = menu;
                                        return true;
                                    }

                                    @Override
                                    public boolean onPrepareActionMode(android.view.ActionMode actionMode, Menu menu) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                                        switch (item.getItemId()) {
                                            case R.id.action_delete:
                                                //alertDialogHelper.showAlertDialog("", "Delete Contact", "DELETE", "CANCEL", 1, false);
                                                if (multiselect_list.size() > 0) {
                                                    for (int i = 0; i < multiselect_list.size(); i++) {
                                                        al.remove(multiselect_list.get(i));
                                                        Entity e = multiselect_list.get(i);
                                                        mydb.deleteNotes(e.getID());
                                                    }
                                                    adapter.notifyDataSetChanged();
                                                }
                                                Toast.makeText(getActivity(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                mode.finish();
                                                return true;
                                            default:
                                                return false;
                                        }
                                    }

                                    @Override
                                    public void onDestroyActionMode(android.view.ActionMode actionMode) {
                                        mActionMode = null;
                                        isMultiSelect = false;
                                        multiselect_list = new ArrayList<Entity>();
                                        refreshAdapter();
                                    }
                                });
                        }
                        multi_select(position);
                    }
                })
        );

        return rootview;
    }

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiselect_list.contains(al.get(position))) {
                multiselect_list.remove(al.get(position));
                pos.remove(position);
            }
            else {
                multiselect_list.add(al.get(position));
                pos.add(position);
            }

            if (multiselect_list.size() > 0)
                mActionMode.setTitle("" + multiselect_list.size());
            else
                mActionMode.setTitle("");

            refreshAdapter();

        }
    }

    public void refreshAdapter() {
        adapter.selected = multiselect_list;
        adapter.nList = al;
        adapter.notifyDataSetChanged();
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
