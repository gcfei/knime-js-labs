org_knime_js_base_node_quickform_filter_column = function() {
	var columnFilter = {
			version: "1.0.0"
	};
	columnFilter.name = "Column filter";
	var viewRepresentation;
	var viewValue;

	columnFilter.init = function(representation, value) {
		var body = $('body');
		viewValue = value;
		viewRepresentation = representation;
		if (viewRepresentation.possibleColumns == null) {
			body.append("Error: No data available");
		} else {
			excludes = $('<select>');
			excludes.css('float', 'left');
			includes = $('<select>');
			includes.css('float', 'left');
			var span = $('<span>');
			span.css('float', 'left');
			var add = $('<button type="button">&gt;</button>');
			add.click(execAdd);
			var addAll = $('<button type="button">&gt;&gt;</button>');
			addAll.click(execAddAll);
			var remove = $('<button type="button">&lt;</button>');
			remove.click(execRemove);
			var removeAll = $('<button type="button">&lt;&lt;</button>');
			removeAll.click(execRemoveAll);
			body.append(excludes);
			body.append(span);
			body.append(includes);
			span.append(add);
			span.append('<br>');
			span.append(addAll);
			span.append('<br>');
			span.append(remove);
			span.append('<br>');
			span.append(removeAll);
			$('select').prop('multiple', true);
			$('select').attr('size', viewRepresentation.possibleColumns.length);

			refreshLists();

			// All buttons get the same width
			var widthMax = Math.max(add.width(), addAll.width(),
					remove.width(), removeAll.width());
			add.width(widthMax);
			addAll.width(widthMax);
			remove.width(widthMax);
			removeAll.width(widthMax);
			// All columns get the same height
			var heightMax = Math.max(excludes.height(), includes.height(), span
					.height());
			excludes.height(heightMax);
			includes.height(heightMax);
			span.height(heightMax);
			// Lists get the same width
			var listWidthMax = Math.max(excludes.width(), includes.width());
			excludes.width(listWidthMax);
			includes.width(listWidthMax);
		}
	};

	columnFilter.value = function() {
		var columns = [];
		includes.find('option').each(function() {
			columns.push($(this).text());
		});
		viewValue.columns = columns;
		return viewValue;
	};

	function refreshLists() {
		excludes.empty();
		includes.empty();
		for ( var i in viewRepresentation.possibleColumns) {
			var column = viewRepresentation.possibleColumns[i];
			var option = $('<option>' + column + '</option>');
			if ($.inArray(column, viewValue.columns) >= 0) {
				option.appendTo(includes);
			} else {
				option.appendTo(excludes);
			}
		}
		refreshListeners();
	}

	function refreshListeners() {
		excludes.find('option').dblclick(execAdd);
		includes.find('option').dblclick(execRemove);
	}

	function execAdd() {
		excludes.find(':selected').each(function() {
			includes.append($(this));
		});
		columnFilter.value();
		refreshLists();
	}

	function execAddAll() {
		excludes.find('option').each(function() {
			includes.append($(this));
		});
		columnFilter.value();
		refreshLists();
	}

	function execRemove() {
		includes.find(':selected').each(function() {
			excludes.append($(this));
		});
		columnFilter.value();
		refreshLists();
	}

	function execRemoveAll() {
		includes.find('option').each(function() {
			excludes.append($(this));
		});
		columnFilter.value();
		refreshLists();
	}
	
	return columnFilter;
	
}();