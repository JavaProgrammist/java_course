let stompClient = null;

function connect() {
    let socket = new SockJS("/gs-guide-websocket");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("Connected: " + frame);
        stompClient.subscribe("/topic/response.clients.add", function (client) {
            handleAddClientResponse(JSON.parse(client.body));
        });
        stompClient.subscribe("/topic/response.clients", function (clients) {
            handleGetAllClientsResponse(JSON.parse(clients.body));
        });
        setConnected(true);
    });
}

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    $("#addClient").prop("disabled", !connected);
    if (connected) {
        displayClients();
    } else {
        clearClientTable();
    }
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function addClient() {
    const clientData = {};
    clientData.name = $("#clientNameTextBox").prop("value");
    clientData.street = $("#clientStreetTextBox").prop("value");

    const clientPhones = $("#clientPhonesTextArea").prop("value");
    if (clientPhones != null && clientPhones.trim() !== "") {
        clientData.phones = clientPhones.trim().split("\n");
    }
    if (!checkClientDataForAdd(clientData)) {
        return;
    }
    stompClient.send("/app/message.clients.add", {}, JSON.stringify(clientData));
}

function checkClientDataForAdd(clientData) {
    if (isStrEmpty(clientData.name)) {
        alert("Не указано имя клиента!");
        return false;
    }
    return true;
}

function isStrEmpty(str) {
    return typeof str === 'undefined' || str === null || str.trim() === "";
}

function displayClients() {
    stompClient.send("/app/message.clients");
}

function handleAddClientResponse(client) {
    $("#clientDataContainer").prop("innerHTML", JSON.stringify(client));
    addClientInTable(client);
}

function handleGetAllClientsResponse(clients) {
    clearClientTable();
    for (let i = 0; i < clients.length; i++) {
        addClientInTable(clients[i]);
    }
}

function clearClientTable() {
    const clientTableBodyRow = $("#clientTableBody tr");
    clientTableBodyRow.remove();
}

function addClientInTable(client) {
    const clientTableBody = $("#clientTableBody")[0];
    let newRow = clientTableBody.insertRow();
    addCellInTableRow(newRow, client.id);
    addCellInTableRow(newRow, client.name);
    addCellInTableRow(newRow, client.street);
    addCellInTableRow(newRow, client.phones.join(", "));
}

function addCellInTableRow(row, cellValue) {
    let newCell = row.insertCell();
    let newText = document.createTextNode(cellValue);
    newCell.appendChild(newText);
}

$(function () {
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#addClient").click(function () {
        addClient();
    });
});