% 2.2
:- use_module(library(lists)).

%estado inicial
estado_inicial(estado(3, 3, 1)).

%estado final
estado_final(estado(0, 0, 0)).

%transições entre estados
sucessor(estado(NM, NC, 1), NovoEstado) :-
	NM1 is NM - 1,
	NC1 is NC - 1,
	NovoEstado = estado(NM1, NC1, 0),
	legal(NovoEstado).
sucessor(estado(NM, NC, 1), NovoEstado) :-
	NM1 is NM - 1,
	NovoEstado = estado(NM1, NC, 0),
	legal(NovoEstado).
sucessor(estado(NM, NC, 1), NovoEstado) :-
	NC1 is NC - 1,
	NovoEstado = estado(NM, NC1, 0),
	legal(NovoEstado).
sucessor(estado(NM, NC, 1), NovoEstado) :-
	NM1 is NM - 2,
	NovoEstado = estado(NM1, NC, 0),
	legal(NovoEstado).
sucessor(estado(NM, NC, 1), NovoEstado) :-
	NC1 is NC - 2,
	NovoEstado = estado(NM, NC1, 0),
	legal(NovoEstado).
sucessor(estado(NM, NC, 0), NovoEstado) :-
	NM1 is NM + 1,
	NC1 is NC + 1,
	NovoEstado = estado(NM1, NC1, 1),
	legal(NovoEstado).
sucessor(estado(NM, NC, 0), NovoEstado) :-
	NM1 is NM + 1,
	NovoEstado = estado(NM1, NC, 1),
	legal(NovoEstado).
sucessor(estado(NM, NC, 0), NovoEstado) :-
	NC1 is NC + 1,
	NovoEstado = estado(NM, NC1, 1),
	legal(NovoEstado).
sucessor(estado(NM, NC, 0), NovoEstado) :-
	NM1 is NM + 2,
	NovoEstado = estado(NM1, NC, 1),
	legal(NovoEstado).
sucessor(estado(NM, NC, 0), NovoEstado) :-
	NC1 is NC + 2,
	NovoEstado = estado(NM, NC1, 1),
	legal(NovoEstado).

legal(estado(NM, NC, _)) :-
	NM >= 0,
	NC >= 0,
	NM =< 3,
	NC =< 3,
	(NM >= NC; NM = 0),
	NMdireita is 3-NM,
	NCdireita is 3-NC,
	(NMdireita >= NCdireita; NMdireita = 0).

mp :- estado_inicial(Ei),
	estado_final(Ef),
	mp(Ei, Ef, [Ei], L), write(L).

mp(E, E, _, [E]).
mp(Ea, Ef, Eants, Sol) :-
	sucessor(Ea, Eseg), \+ member(Eseg, Eants),
	mp(Eseg, Ef, [Eseg|Eants], R), Sol = [Ea|R].

pl :- estado_inicial(Ei), estado_final(Ef),
	pl([[Ei]], Ef, Sol), reverse(Sol, ReversedSol), write(ReversedSol).

pl([CaminhoAtual | _], Ef, CaminhoAtual) :- CaminhoAtual = [Ef | _].
pl([CaminhoAtual | Outros], Ef, Sol) :- CaminhoAtual = [Ea | OutrosEstados],
	findall([EstadoSeguinte | CaminhoAtual], (sucessor(Ea, EstadoSeguinte), \+ member(EstadoSeguinte, OutrosEstados)), NovosCaminhos),
	append(Outros, NovosCaminhos, NovaAnv),
	pl(NovaAnv, Ef, Sol).
