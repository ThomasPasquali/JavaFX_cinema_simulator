select p.*, s.*
from prenotazioni as p
	join spettacoli as s on p.Spettacolo = s.ID
where p.Account = 2
	 and if(s.data > CURRENT_DATE(), true, if(s.data = CURRENT_DATE(), s.Ora > CURRENT_TIME(), false))
