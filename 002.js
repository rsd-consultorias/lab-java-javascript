function execute(params) {
    params = JSON.parse(params); 
    console.log(rd.getContractId());

    let amount = params ? params.amount : 1000;
    rd.deposit(amount);
    output.deposits = rd.getBalance().getAmount();

    rd.withdraw(200);
    output.withdraws = rd.getBalance().getAmount();

    rd.withdraw(200);
    output.withdraws = rd.getBalance().getAmount();

    rd.withdraw(200);
    output.withdraws = rd.getBalance().getAmount();

    rd.withdraw(500);
    output.withdraws = rd.getBalance().getAmount();

    output.finalBalance = rd.getBalance().getAmount();
    output.user = rd.isUserAuthorized('teste');
    output.contract = '002';
    output.log = rd.getAuditLog();
}

function test() {
    execute({ amount: 1000 });
}