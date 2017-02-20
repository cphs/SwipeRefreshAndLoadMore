package com.cphs.swiperefreshandloadmore;

import java.util.LinkedList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;

public class MainActivity extends Activity {

  private final int MAXPAGE = 3;
  private LoadMoreListView loadMoreListView;
  private SwipeRefreshLayout swipeRefreshLayout;
  private LinkedList<String> datas;
  private ArrayAdapter<String> adapter;
  private int currentPage = 0;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    loadMoreListView = (LoadMoreListView) findViewById(R.id.lv_item);
    swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_main);
    datas = new LinkedList<>();

    setDefaultData();

    loadMoreListView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
      @Override
      public void onLoadMore() {
        if (currentPage < MAXPAGE) {
          new FakeLoadmoreAsync().execute();
        } else {
          loadMoreListView.onLoadMoreComplete();
        }
      }
    });

    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        new FakePullRefreshAsync().execute();
      }
    });
  }

  private void setDefaultData() {
    String[] androidVersion = new String[] {"Not Apple", "Not Blackberry", "Cupcake", "Donut",
        "Eclair", "Froyo", "Gingerbread", "Honeycomb", "Ice cream sandwich", "Jelly Bean", "Kitkat",
        "Lollipop", "Marshmallow", "Nougat"};

    for (int i = 0; i < androidVersion.length; i++) {
      datas.add(androidVersion[i]);
    }

    adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,
        android.R.id.text1, datas);
    loadMoreListView.setAdapter(adapter);
  }

  private void getLoadMoreData() {
    String loadmoreText = "Loadmore....";
    for (int i = 0; i < 5; i++) {
      datas.addLast(loadmoreText);
    }
    adapter.notifyDataSetChanged();
    loadMoreListView.onLoadMoreComplete();
    loadMoreListView.setSelection(datas.size() - 11);
  }

  private void getPullRefreshData() {
    String swipePullRefreshText = "Swipe....";
    for (int i = 0; i < 5; i++) {
      datas.addFirst(swipePullRefreshText);
    }
    swipeRefreshLayout.setRefreshing(false);
    adapter.notifyDataSetChanged();
    loadMoreListView.setSelection(0);
  }

  /**
   * Fake class to create fake loading
   */
  private class FakeLoadmoreAsync extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
      try {
        Thread.sleep(3000);
      } catch (Exception e) {
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      getLoadMoreData();
      currentPage += 1;
    }
  }

  /**
   * Fake class to create fake loading
   */
  private class FakePullRefreshAsync extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
      try {
        Thread.sleep(3000);
      } catch (Exception e) {
      }
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      getPullRefreshData();
    }
  }
}
