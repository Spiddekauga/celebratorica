package com.spiddekauga.celebratorica.item;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.spiddekauga.android.AppFragmentHelper;
import com.spiddekauga.android.ui.SnackbarHelper;
import com.spiddekauga.celebratorica.R;
import com.spiddekauga.utils.EventBus;

/**
 * Adds a new item to a category
 */
public class ItemAddFragment extends ItemDialogFragment {
@Override
public boolean onMenuItemClick(MenuItem item) {
	if (validateTextFields()) {
		addItem();
	}
	return true;
}

private void addItem() {
	Item item = new Item();
	setItemFromFields(item);
	
	ItemEvent itemEvent = new ItemEvent(item, ItemEvent.Actions.ADD);
	EventBus.getInstance().post(itemEvent);
	SnackbarHelper.showSnackbar(R.string.item_add_success);
	
	dismiss();
}

@Nullable
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	View view = super.onCreateView(inflater, container, savedInstanceState);
	
	AppFragmentHelper.focusEditText(getTextField());
	
	
	setBackMessage(R.string.item_add_discard);
	
	return view;
}

@Override
protected int getTitle() {
	return R.string.item_add_header;
}

@Override
protected int getMenu() {
	return R.menu.menu_save;
}
}
