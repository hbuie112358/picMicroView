;;;;;;; slowToggleAllPorts.asm ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
;;;;;;; Program hierarchy ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;
; Mainline
;  
;
;;;;;;; Assembler directives ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

        list  P=PIC18F452, F=INHX32, C=160, N=0, ST=OFF, MM=OFF, R=DEC, X=ON
        #include p18f452.inc

;;;;;;; Variables ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

			cblock  0x000           ;Beginning of Access RAM
			temp
			temp1
			temp2
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
		movlw		0
		movwf		PORTA
		movwf		PORTB
		movwf		PORTC
		movwf		PORTD
		movwf		PORTE

wrap		movlw		39
		movwf		temp
decw		decf		temp, f
		bz		toggle
		goto		outer
		
outer		movlw		255
		movwf		temp1
dec1		decf		temp1, f
		bz		decw
rc		rcall		inner
		goto		dec1
inner		movlw		255
		movwf		temp2
dec		decf		temp2, f
		bnz		dec
		return

toggle		btg		PORTA, RA2
		btg		PORTB, RA3
		btg		PORTC, RA4
		btg		PORTD, RA5
		btg		PORTE, RA1
		btg		PORTA, RA6
		btg		PORTB, RA6
		btg		PORTC, RA5
		btg		PORTD, RA4
		btg		PORTE, RA6
		goto 		wrap
stop		goto		stop

		end
