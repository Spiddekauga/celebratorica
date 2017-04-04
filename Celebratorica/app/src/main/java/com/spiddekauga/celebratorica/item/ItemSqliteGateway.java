package com.spiddekauga.celebratorica.item;

import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;

import com.spiddekauga.android.sqlite.SqliteGateway;
import com.spiddekauga.celebratorica.R;
import com.spiddekauga.celebratorica.util.AppActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Gateway for getting celebration and list items
 */
class ItemSqliteGateway extends SqliteGateway {
/**
 * Get all categories
 * @return list of all categories
 */
List<Category> getCategories() {
	Resources resources = AppActivity.getActivity().getResources();
	
	String sql = "SELECT " +
			resources.getString(R.string.table_list_id) + ", " +
			resources.getString(R.string.table_list_name) + ", " +
			resources.getString(R.string.table_list_order) +
			" FROM " + resources.getString(R.string.table_list) +
			" ORDER BY " + resources.getString(R.string.table_list_order) + " ASC";
	
	Cursor cursor = rawQuery(sql);
	List<Category> categories = new ArrayList<>(cursor.getCount());
	while (cursor.moveToNext()) {
		Category category = new Category();
		int i = 0;
		category.setCategoryId(cursor.getLong(i++));
		category.setName(cursor.getString(i++));
		category.setOrder(cursor.getInt(i));
		
		categories.add(category);
	}
	close(cursor);
	
	return categories;
}

/**
 * Get all items in the specified category
 * @param categoryId the category id to get the celebration from
 * @return list of all items in the category list
 */
List<Item> getItems(long categoryId) {
	Resources resources = AppActivity.getActivity().getResources();
	
	String sql = "SELECT " +
			resources.getString(R.string.table_item_id) + ", " +
			resources.getString(R.string.table_item_text) + ", " +
			resources.getString(R.string.table_item_date) +
			" FROM " + resources.getString(R.string.table_item) +
			" WHERE " + resources.getString(R.string.table_list_id) + "=" + categoryId +
			" ORDER BY " +
			resources.getString(R.string.table_item_date) + " DESC, " +
			resources.getString(R.string.table_item_id) + " DESC";
	
	
	Cursor cursor = rawQuery(sql);
	List<Item> items = new ArrayList<>(cursor.getCount());
	while (cursor.moveToNext()) {
		Item item = new Item();
		item.setCategoryId(categoryId);
		int i = 0;
		item.setItemId(cursor.getLong(i++));
		item.setText(cursor.getString(i++));
		item.setDate(cursor.getLong(i));
		
		items.add(item);
	}
	close(cursor);
	
	return items;
}

/**
 * Add a new category. Will automatically set the category id
 * @param category the category to add
 */
void addCategory(Category category) {
	Resources resources = AppActivity.getActivity().getResources();
	
	ContentValues contentValues = new ContentValues();
	contentValues.put(resources.getString(R.string.table_list_name), category.getName());
	
	// Order
	int order;
	if (category.getOrder() > 0) {
		order = category.getOrder();
	} else {
		order = getCategoryCount() + 1;
	}
	contentValues.put(resources.getString(R.string.table_list_order), order);
	
	// Category id
	if (category.getCategoryId() > 0) {
		contentValues.put(resources.getString(R.string.table_list_id), category.getCategoryId());
	}
	
	long id = insert(resources.getString(R.string.table_list), contentValues);
	category.setCategoryId(id);
}

/**
 * @return number of categories
 */
private int getCategoryCount() {
	Resources resources = AppActivity.getActivity().getResources();
	
	String sql = "SELECT " +
			resources.getString(R.string.table_list_order) +
			" FROM " + resources.getString(R.string.table_list) +
			" ORDER BY " + resources.getString(R.string.table_list_order) + " DESC" +
			" LIMIT 1";
	
	Cursor cursor = rawQuery(sql);
	int count = 0;
	if (cursor.moveToNext()) {
		count = cursor.getInt(0);
	}
	close(cursor);
	
	return count;
}

/**
 * Update a category.
 * @param category the category to update
 */
void updateCategory(Category category) {
	Resources resources = AppActivity.getActivity().getResources();
	
	ContentValues contentValues = new ContentValues(2);
	contentValues.put(resources.getString(R.string.table_list_name), category.getName());
	contentValues.put(resources.getString(R.string.table_list_order), category.getOrder());
	
	String table = resources.getString(R.string.table_list);
	String where = resources.getString(R.string.table_list_id) + "=" + category.getCategoryId();
	update(table, contentValues, where);
}

/**
 * Remove category.
 * @param category the category to remove.
 */
void removeCategory(Category category) {
	Resources resources = AppActivity.getActivity().getResources();
	
	String table = resources.getString(R.string.table_list);
	String where = resources.getString(R.string.table_list_id) + "=" + category.getCategoryId();
	delete(table, where);
}

/**
 * Add a new item. Will automatically set the item id.
 * @param item the item to add
 */
void addItem(Item item) {
	Resources resources = AppActivity.getActivity().getResources();
	
	ContentValues contentValues = new ContentValues();
	contentValues.put(resources.getString(R.string.table_list_id), item.getCategoryId());
	contentValues.put(resources.getString(R.string.table_item_text), item.getText());
	contentValues.put(resources.getString(R.string.table_item_date), item.getDateTime());
	
	// If we have an id, use that
	if (item.getItemId() != -1) {
		contentValues.put(resources.getString(R.string.table_item_id), item.getItemId());
	}
	
	long id = insert(resources.getString(R.string.table_item), contentValues);
	item.setItemId(id);
}

/**
 * Update an item.
 * @param item the item to update
 */
void updateItem(Item item) {
	Resources resources = AppActivity.getActivity().getResources();
	
	ContentValues contentValues = new ContentValues(2);
	contentValues.put(resources.getString(R.string.table_item_text), item.getText());
	contentValues.put(resources.getString(R.string.table_item_date), item.getDateTime());
	
	String table = resources.getString(R.string.table_item);
	String where = resources.getString(R.string.table_item_id) + "=" + item.getItemId();
	update(table, contentValues, where);
}

/**
 * Remove an item.
 * @param item the item item to remove
 */
void removeItem(Item item) {
	Resources resources = AppActivity.getActivity().getResources();
	
	String table = resources.getString(R.string.table_item);
	String where = resources.getString(R.string.table_item_id) + "=" + item.getItemId();
	delete(table, where);
}
}
