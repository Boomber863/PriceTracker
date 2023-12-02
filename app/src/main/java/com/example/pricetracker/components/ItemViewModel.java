package com.example.pricetracker.components;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pricetracker.dto.response.ItemResponse;

import java.util.List;

public class ItemViewModel extends ViewModel {

    private final MutableLiveData<List<ItemResponse>> allItemsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<ItemResponse>> followedItemsLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<ItemResponse>> notFollowedItemsLiveData = new MutableLiveData<>();

    public LiveData<List<ItemResponse>> getAllItemsLiveData() {
        return allItemsLiveData;
    }

    public LiveData<List<ItemResponse>> getFollowedItemsLiveData() {
        return followedItemsLiveData;
    }

    public LiveData<List<ItemResponse>> getNotFollowedItemsLiveData() {
        return notFollowedItemsLiveData;
    }

    public void setAllItems(List<ItemResponse> allItems) {
        allItemsLiveData.setValue(allItems);
    }

    public void setFollowedItems(List<ItemResponse> notFollowedItems) {
        followedItemsLiveData.setValue(notFollowedItems);
    }

    public void setNotFollowedItems(List<ItemResponse> notFollowedItems) {
        notFollowedItemsLiveData.setValue(notFollowedItems);
    }

    public void addFollowedItem(ItemResponse selectedItem) {
        List<ItemResponse> currentList = followedItemsLiveData.getValue();
        if (currentList != null) {
            currentList.add(selectedItem);
            followedItemsLiveData.setValue(currentList);
        }
    }

    public void removeFollowedItem(ItemResponse selectedItem) {
        List<ItemResponse> currentList = followedItemsLiveData.getValue();
        if (currentList != null) {
            currentList.remove(selectedItem);
            followedItemsLiveData.setValue(currentList);
        }
    }
}
