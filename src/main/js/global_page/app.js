/*
 * Copyright (C) 2009-2020 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

import { metrics } from "./metrics";
let el, stylesTag;

//<h2>This is a heading in a div element</h2>
//<p>This is some text in a div element.</p>


function init() {
  console.log("Init")
  el.innerHTML = `
  <body>
    <div id="myData"></div>
  </body>
`;
console.log("End Init")
}


function appendData(data) {
  var mainContainer = document.getElementById("myData");
  console.log(data.ID)
  for (var i = 0; i < data.length; i++) {
      var div = document.createElement("div");
      div.innerHTML = 'ID: ' + data[i].ID+ ' ' + data[i].value;
      mainContainer.appendChild(div);
  }
}

//.then(res => res.text())          // convert to plain text
//.then(text => console.log(text))  // then log it out//

export function start(element) {
  el = element;
  init();
  DisplayMetricsTable();

  /*
  
  fetch('./metrics.json').then(response => {
      console.log(response);
      console.log("In Here")
      return response.text();

    }).then(data => {
      // Work with JSON data here
      console.log("Read Data successfully " + data);
    }).catch(err => {
        // Do something for an error here
        console.log("Error Reading data " + err);
     });
     */

}

export function stop() {
  // Remove any event listeners we still have.
}




function DisplayMetricsTable() {
  //Build an array containing Customer records.

  const hdr = ["Filename", "Metric", "Category","Title","Description","Value"];
  console.log("Display Metrics Table"); 

 /* var catcolors = {
    "size": "lightgray",
    "complexity": "tan",
    "coupling": "pink",
    "cohesion": "slategray",
    "encapsulation" : "thistle"
  };*/

  var catcolors = {  
    "size": "whitesmoke",
    "complexity": "wheat",
    "coupling": "lavender",
    "cohesion": "mintcream",
    "encapsulation" : "mistyrose"
};
    
  var columnCount = hdr.length;


  var table = document.createElement("table"), data,row, cell;
  table.border = '3';

  //Add the header row.
  row = table.insertRow(-1);
  row.bgColor = 'lightblue';
  for (var i = 0; i < columnCount; i++) {
      //var headerCell = document.createElement("TH");
      //headerCell.innerHTML = hdr[i];

      cell = row.insertCell(-1);
      cell.style.padding = '5px'
      cell.style.spacing = '5px'
      //cell.style.width = '200px';
      cell.innerHTML = hdr[i];
      //row.appendChild(headerCell);

  }

  //Add the data rows.
  for (var i = 0; i < metrics.length; i++) {
    data = metrics[i]
    row = table.insertRow();
    var category = data["category"]
    row.bgColor = catcolors[category]
    for (let key in metrics[i]) {
      cell = row.insertCell(-1);
      cell.style.padding = '5px'
      cell.style.spacing = '5px'
      //cell.style.width = '200px';
      cell.innerHTML = data[key];
    }
  }

  var mTable = document.getElementById("myData");
  mTable.innerHTML = "";
  mTable.appendChild(table);
}