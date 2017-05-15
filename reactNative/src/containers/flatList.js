
import React, { Component } from 'react';

import {
	Text,
	View,
	FlatList,
	StyleSheet,
	Animated,
	TouchableHighlight
} from 'react-native';

import Dimensions from 'Dimensions';

//	Redux
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';

var jsonlist = require('../assets/longlist');

//	Styles
import containerStyle from '../styles/container';

//	Redux actions
import * as LongListActions from '../actions/longListActions';

import Swipeable from 'react-native-swipeable';
import ListRow from '../components/listRow';

var start;

class FlatListList extends Component {
	constructor(props){
		super(props);
		this.row = this.row.bind(this);
	}


	componentWillMount() {
		//	Wonder if this should be moved to background thread...
		if (this.props.rows.items === undefined || this.props.rows.length == 0) {
			
			this.props.actions.fill(jsonlist);

		}
	}

	componentDidMount(){
		this.rightValue = new Animated.Value(0);
		this.deleteItem = this.deleteItem.bind(this);
		this._editItem = this._editItem.bind(this);
		this._shouldItemUpdate = this._shouldItemUpdate.bind(this);
	}

	//	Might not need this for this type of list
	componentWillReceiveProps(props) {
		if(props.rows.length < this.refs.flatList.props.data.length){
			//	Removed item
			let stop = performance.now();
			console.log("Time taken: ", stop - start);
		}
		//	Check if a new was added
		//console.log("NEW PROPS: ", props);
		if (this.refs.flatList.props.data.length < props.rows.length) {
			//	new
			this.refs.flatList.scrollToEnd({ animated: true });
		}
	}

	/**
	 * Row component for the flat list. Expected to receive param item with the actual data and
	 * 	index which is the list's index
	 * @param {item} rowdata  
	 */
	row({ item, index }) {
		return (
			<ListRow 
				key={item._id}
				data={item} 
				index={index} 
				deleteItem={this.deleteItem}
				editItem={this._editItem}
			/>
		);
	}

	deleteItem(index) {
		start = performance.now();
		this.props.actions.removeItem(index)
	}

	_editItem(index){
		this.props.actions.editItem(index);
	}

	_shouldItemUpdate( prev, next ){
		//console.log("SHOULDITEMUPDATE: ", this.props.rows[prev.index], "NEXT: ", this.props.rows[next.index] );
		//return next !== this.props.rows[next.index]
		return prev !== next;
	}
	_getItemLayout(data,index){
		return {
			length: 75, offset: 75 * index, index
		}
	}

	render() {
		const { rows } = this.props;
		var i = 0;

		return (

			<View style={containerStyle.defaultContainer}>
				<FlatList
					style={styles.listStyle}
					data={rows}
					keyExtractor={(item, index) => item._id}
					ref="flatList"
					renderItem={this.row}
					ItemSeparatorComponent={(item, index) => <View key={index} style={styles.separator} />}

					getItemLayout={this._getItemLayout}
				/>
			</View>

		);
	}
}

const styles = StyleSheet.create({
	listStyle: {
		padding: 8
	},
	separator: {
		flex: 1,
		height: StyleSheet.hairlineWidth,
		backgroundColor: "#222",
	},
})

export default connect((state) => ({
	rows: state.LongListReducer
}), (dispatch) => ({
	actions: bindActionCreators(LongListActions, dispatch)
})
)(FlatListList);

