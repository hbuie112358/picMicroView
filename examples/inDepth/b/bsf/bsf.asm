;;;;;;; bsf.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;This program tests operation of bsf instruction. Tests instruction according
;to pic18 user manual examples.
;
;
;;;;;;; Program hierarchy ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Mainline
;  
;
;;;;;;; Assembler directives ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

	#include p18f452.inc
        list  P=PIC18F452, F=INHX32, C=160, N=0, ST=OFF, MM=OFF, R=DEC, X=ON

;;;;;;; Variables ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

	cblock  0x100           ;Beginning of Access RAM
	temp0			;This many registers are necessary so that the 
	temp1			;test can satisfy the condition in the manual
	temp2			;that the assembler determine that FLAG_REG 
	temp3			;requires access bit to be set for the first 
	temp4			;example. Otherwise, the "a" flag could have 
	temp5			;been used in the instruction "bsf FLAG_REG, 7"
	temp6			;like so: "bsf FLAG_REG, 7, 1". Assigns FLAG_REG
	temp7			;address 0x180 instead of 0x080 so that SFR's 
	temp8			;which are mapped to 0x80 - 0xff are not overwritten.
	temp9
	temp10
	temp11
	temp12
	temp13
	temp14
	temp15
	temp16
	temp17
	temp18
	temp19
	temp20
	temp21
	temp22
	temp23
	temp24
	temp25
	temp26
	temp27
	temp28
	temp29
	temp30
	temp31
	temp32
	temp33
	temp34
	temp35
	temp36
	temp37
	temp38
	temp39
	temp40
	temp41
	temp42
	temp43
	temp44
	temp45
	temp46
	temp47
	temp48
	temp49
	temp50
	temp51
	temp52
	temp53
	temp54
	temp55
	temp56
	temp57
	temp58
	temp59
	temp60
	temp61
	temp62
	temp63
	temp64
	temp65
	temp66
	temp67
	temp68
	temp69
	temp70
	temp71
	temp72
	temp73
	temp74
	temp75
	temp76
	temp77
	temp78
	temp79
	temp80
	temp81
	temp82
	temp83
	temp84
	temp85
	temp86
	temp87
	temp88
	temp89
	temp90
	temp91
	temp92
	temp93
	temp94
	temp95
	temp96
	temp97
	temp98
	temp99
	temp100
	temp101
	temp102
	temp103
	temp104
	temp105
	temp106
	temp107
	temp108
	temp109
	temp110
	temp111
	temp112
	temp113
	temp114
	temp115
	temp116
	temp117
	temp118
	temp119
	temp120
	temp121
	temp122
	temp123
	temp124
	temp125
	temp126
	temp127
	FLAG_REG
	endc

;;;;;;; Macro definitions ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;;;;;;; Vectors ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


        org  0x0000             ;Reset vector
        nop
        goto  Mainline

        org  0x0008             ;High priority interrupt vector
        goto  $                 ;Trap

        org  0x0018             ;Low priority interrupt vector
        goto  $                 ;Trap

;;;;;;; Mainline program ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

Mainline

;Example 1:
		movlb		1		;Place 1 in BSR for bank 1
		movlw		10		;Place 10 in wreg
		movwf		FLAG_REG	;Copy wreg value to FLAG_REG
		bsf		FLAG_REG, 7	;Set bit 7 of FLAG_REG

;Example 2:
		movlw		0x20		;Place 0x20 in wreg
		lfsr		0, 0x0c2	;Place 0x0c2 in FSR0H:FSR0L
		movwf		INDF0		;Copy wreg into register pointed to by FSR0
		movlw		0x17		;Place 0x17 in wreg
		bsf		INDF0, 3	;Set bit 3 in register pointed to 
						;by FSR0H:FSR0L


stop		goto		stop

		end






















