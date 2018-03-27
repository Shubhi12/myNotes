package com.saloni.mynotes;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.saloni.mynotes.R.layout.grid_single;


public class Home2 extends Fragment {

    public static String name = "";
   RecyclerView recyclerView;
    ArrayList<Entity> selected_list=new ArrayList<>();
    ArrayList<Entity> list=new ArrayList<>();
    GridAdapter adapter;
    FloatingActionButton btnplus;
    boolean isMultiSelect = false;
    Menu context_menu;
    android.view.ActionMode mActionMode;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home2, viewGroup, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view2);
        btnplus = (FloatingActionButton) view.findViewById(R.id.btnplus);
        btnplus.setOnClickListener(new View.OnClickListener() {
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
        final NDb mydb = new NDb(getActivity());
        list=mydb.getAll();
        adapter= new GridAdapter(getActivity(), list, selected_list);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new Home2.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(),recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (isMultiSelect) {
                            multi_select(position);
                        } else {
                            CardView cv = (CardView) view;
                            LinearLayout linearLayoutChild = (LinearLayout) cv
                                    .getChildAt(0);
                            TextView m = (TextView) linearLayoutChild.getChildAt(0);
                            Bundle dataBundle = new Bundle();
                            dataBundle.putInt("id",
                                    Integer.parseInt(m.getText().toString()));
                            Intent intent = new Intent(getActivity(),
                                    EditNote.class);
                            intent.putExtras(dataBundle);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        if (!isMultiSelect) {
                            selected_list = new ArrayList<Entity>();
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
                                                if (selected_list.size() > 0) {
                                                    for (int i = 0; i < selected_list.size(); i++) {
                                                        list.remove(selected_list.get(i));
                                                        Entity e =selected_list.get(i);
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
                                        selected_list = new ArrayList<Entity>();
                                        refreshAdapter();
                                    }
                                });
                        }
                        multi_select(position);
                    }
                })
        );


        return view;
   }

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (selected_list.contains(list.get(position))) {
                selected_list.remove(list.get(position));
            }
            else {
               selected_list.add(list.get(position));
            }

            if (selected_list.size() > 0)
                mActionMode.setTitle("" + selected_list.size());
            else
                mActionMode.setTitle("");

            refreshAdapter();

        }
    }

    public void refreshAdapter() {
        adapter.selected = selected_list;
        adapter.nList = list;
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

