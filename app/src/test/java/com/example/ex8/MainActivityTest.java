package com.example.ex8;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 28)
public class MainActivityTest extends TestCase {

  private ActivityController<MainActivity> activityController;
  private RootHolderImpl mockHolder;

  @Before
  public void setup() {
    mockHolder = Mockito.mock(RootHolderImpl.class);
    // when asking the `mockHolder` to get the current items, return an empty list
    Mockito.when(mockHolder.getCurrentItems())
            .thenReturn(new ArrayList<>());

    activityController = Robolectric.buildActivity(MainActivity.class);

    // let the activity use our `mockHolder` as the TodoItemsHolder
    MainActivity activityUnderTest = activityController.get();
    activityUnderTest.holder = mockHolder;
  }

  @Test
  public void when_activityIsLaunched_then_theEditTextStartsEmpty() {
    // setup
    activityController.create().visible();
    MainActivity activityUnderTest = activityController.get();
    EditText editText = activityUnderTest.findViewById(R.id.editTextInsertTask);
    String userInput = editText.getText().toString();
    // verify
    assertTrue(userInput.isEmpty());
  }

  @Test
  public void when_holderSaysNoItems_then_recyclerViewShowsZeroItems() {
    // setup
    Mockito.when(mockHolder.getCurrentItems())
            .thenReturn(new ArrayList<>());

    // test - let the activity think it is being shown
    activityController.create().visible();

    // verify
    MainActivity activityUnderTest = activityController.get();
    RecyclerView recyclerView = activityUnderTest.findViewById(R.id.recyclerTodoItemsList);
    RecyclerView.Adapter adapter = recyclerView.getAdapter();
    assertNotNull(adapter);
    assertEquals(0, adapter.getItemCount());
  }

  @Test
  public void when_holderSays1ItemOfTypeInProgress_then_activityShouldShow1MatchingViewInRecyclerView() {
    // setup

    // when asking the `mockHolder` to get the current items, return a list with 1 item of type "in progress"
    ArrayList<RootCalc> itemsReturnedByHolder = new ArrayList<>();
    Mockito.when(mockHolder.getCurrentItems())
            .thenReturn(itemsReturnedByHolder);
    RootCalc itemInProgress = new RootCalc(100L, UUID.randomUUID());
    itemsReturnedByHolder.add(itemInProgress);

    // test - let the activity think it is being shown
    activityController.create().visible();

    // verify: make sure that the activity shows a matching subview in the recycler view
    MainActivity activityUnderTest = activityController.get();
    RecyclerView recyclerView = activityUnderTest.findViewById(R.id.recyclerTodoItemsList);

    // 1. verify that adapter says there should be 1 item showing
    RecyclerView.Adapter adapter = recyclerView.getAdapter();
    assertNotNull(adapter);
    assertEquals(1, adapter.getItemCount());

    // 2. verify that the shown view has a checkbox being not-checked and has a TextView showing the correct description
    View viewInRecycler = recyclerView.findViewHolderForAdapterPosition(0).itemView;
    //was a todo:
    TextView desc = viewInRecycler.findViewById(R.id.roots);
    assertEquals(desc.getText(), "10x10=100");
  }
}
