import {Component, OnInit, ElementRef, ViewChild} from '@angular/core';

import { ListView } from "ui/list-view";
import { View } from "ui/core/view";
import { Page } from "ui/page";

import ListItem from '../../models/listItem';

var longListImport = require("../../longlist.json");
//	Get rid of nasty red underline
declare var NSIndexPath;
declare var UITableViewScrollPosition;

@Component({
  selector: "my-app",
  templateUrl: "pages/longList/longList.html",
  styleUrls: ["pages/longList/longList-common.css", "pages/longList/longList.css"]
})
export default class LongListPage implements OnInit{

	longListList: Array<ListItem> = longListImport;
	@ViewChild("longListTemplate") longListRef: ElementRef;

	constructor(private page: Page){}

	ngOnInit(){

	}

	editItem(index){
		this.longListList[index].firstName = "new FirstName";
		this.longListList[index].lastName = "new LastName";
		 
	}

	deleteItem(index){
		let list = <ListView>this.longListRef.nativeElement;
		const deletingView = list.getViewById(index);
		deletingView.animate({
			//	Shrink view
			scale: {x: 0, y: 0.5},
			opacity: 0,
			duration: 300
		}).then(()=>{
			this.longListList.splice(index, 1);	
			//	Revert shrink 
			deletingView.animate({
				scale: {x: 1, y: 1},
				opacity: 1,
				duration: 0
			});
		});
	}
 
 	//	Currently not scrolling to bottom
	addItem(){
		this.longListList.push(new ListItem("SomeID", "dummy firstName", "dummy lastName"));
		let list = <ListView>this.longListRef.nativeElement;
		
		//	Scroll to bottom
		console.log(this.longListList.length); 
		if(list.ios){
			//list.ios.scrollToRowAtIndexPath(NSIndexPath.indexPathWithIndex(this.longListList.length - 1), UITableViewScrollPosition.bottom, true);
			list.ios.scrollToRowAtIndexPathAtScrollPositionAnimated(
            NSIndexPath.indexPathForItemInSection(this.longListList.length - 2, 0),
            UITableViewScrollPosition.UITableViewScrollPositionTop,
            true
        );
		}
		else{
			//	Might have to fix this. cant be scrolling for 5 minutes
			list.android.smoothScrollToPosition(this.longListList.length - 1);
		}

		//list.android.setSelection(200);
	}
}