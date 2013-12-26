package fw.game.serverpackets;

import fw.game.GameEngine;
import fw.dbClasses.DbL2ItemBase;
import fw.dbClasses.DbL2Npc;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Vector;
import fw.dbClasses.DbL2Skill;


public class SystemMessage extends ServerBasePacket
{
	// d d (d S/d d/d dd)
	//      |--------------> 0 - String  1-number 2-textref npcname (1000000-1002655)  3-textref itemname 4-textref skills 5-??
	private static Vector<Integer> _types = new Vector<Integer>();
	private static Vector<Object> _values = new Vector<Object>();
    private static GameEngine _clientThread;
    private int _messageId;
    private int _typesSize;

	private static final int TYPE_SKILL_NAME = 4;
	private static final int TYPE_ITEM_NAME = 3;
	private static final int TYPE_NPC_NAME = 2;
	private static final int TYPE_NUMBER = 1;
	private static final int TYPE_TEXT = 0;


	public SystemMessage(GameEngine clientThread, byte data[])
	{
		_clientThread = clientThread;
		_buf = ByteBuffer.wrap(data);
		_buf.order(ByteOrder.LITTLE_ENDIAN);
//		readH(); // msg length
		readC(); // msg type

		_messageId = readD();
		_typesSize = readD();


		for (int i = 0; i < _typesSize; i++)
		{
			int t = readD();

			switch (t)
			{
			case TYPE_TEXT:
			{
				addString(readS());
				break;
			}
			case TYPE_NUMBER:
			{
				addNumber(readD());
				break;
			}
			case TYPE_NPC_NAME:
			{
				DbL2Npc l2Npc = clientThread.getDbObjects().getDbL2Npc(readD() - 1000000);
				addNpcName(l2Npc.getName());
				break;
			}
			case TYPE_ITEM_NAME:
			{
				DbL2ItemBase l2Item = clientThread.getDbObjects().getDbL2Item(readD());
				addItemName(l2Item.getName());
				break;
			}
			case TYPE_SKILL_NAME:
			{
				DbL2Skill l2Skill = clientThread.getDbObjects().getDbL2Skill(readD());
				addSkillName(l2Skill.getName());
				break;
			}

		  }
		}

	}

	public void runImpl()
	{
			switch (_messageId)
			{
				case 0:{
					sendToInterface("You have been disconnected from the server.",GameEngine.MSG_SYSTEM_NORMAL,true); break;
				}
				case 1:{
					sendToInterface("The server will be coming down in "+_values.get(0)+" second(s).",GameEngine.MSG_SYSTEM_NORMAL,true); break;
				}
				case 3:{
					sendToInterface(_values.get(0)+" is not currently logged in.",GameEngine.MSG_SYSTEM_NORMAL,true); break;
				}
				case 4:{
					sendToInterface("You cannot ask yourself to apply to a clan.",GameEngine.MSG_SYSTEM_NORMAL,true); break;
				}
				case 5:{
					sendToInterface(_values.get(0)+" already exist.",GameEngine.MSG_SYSTEM_NORMAL,true); break;
				}
				case 6:{
					sendToInterface(_values.get(0)+" does not exist.",GameEngine.MSG_SYSTEM_NORMAL,true); break;
				}
				case 7:{
					sendToInterface("Cant use this inside Boss Zones.",GameEngine.MSG_SYSTEM_NORMAL,true); break;
				}
				case 8:{
					sendToInterface("You are working with another clan.",GameEngine.MSG_SYSTEM_NORMAL,true); break;
				}
				case 9:{
					sendToInterface(_values.get(0)+" is not a clan leader.",GameEngine.MSG_SYSTEM_NORMAL,true); break;
				}
				case 10:{
					sendToInterface(_values.get(0)+" is working with another clan.",GameEngine.MSG_SYSTEM_NORMAL,true); break;
				}
				case 11:{
					sendToInterface("Can't Summon target Inside Boss Zone.",GameEngine.MSG_SYSTEM_NORMAL,true); break;
				}
				case 13:{
					sendToInterface("Unable to disperse: your clan has requested to participate in a castle siege.",GameEngine.MSG_SYSTEM_NORMAL,true); break;
				}
				case 22:{
					sendToInterface("Your target is out of range.",GameEngine.MSG_SYSTEM_NORMAL,true); break;
				}
				case 23:{
					sendToInterface("Not enough HP.",GameEngine.MSG_SYSTEM_NORMAL,true); break;
				}
				case 24:{
					sendToInterface("Not enough MP.",GameEngine.MSG_SYSTEM_NORMAL,true); break;
				}
				case 25:{
					sendToInterface("Rejuvenating HP.",GameEngine.MSG_SYSTEM_NORMAL,true); break;
				}
				case 26:{
					sendToInterface("Rejuvenating MP.",GameEngine.MSG_SYSTEM_NORMAL,true); break;
				}
				case 27:{
					sendToInterface("Your casting has been interrupted.",GameEngine.MSG_SYSTEM_NORMAL,true); break;
				}
				case 28:{
					sendToInterface("You have obtained "+_values.get(0)+" adena.",GameEngine.MSG_COMBAT,true); break;
				}
				case 29:{
					sendToInterface("You have obtained  "+_values.get(0)+" "+_values.get(1),GameEngine.MSG_COMBAT,true); break;
				}
				case 30:{
					sendToInterface("You have obtained "+_values.get(0),GameEngine.MSG_COMBAT,true);	break;
				}
				case 31:{
					sendToInterface("You cannot move while sitting.",GameEngine.MSG_CHAT_ALL,false);	break;
				}
			   	case 34:{
					sendToInterface("Welkome to the World of LineageII.",GameEngine.MSG_CHAT_ALL,true);	break;
				}
				case 35:{
					sendToInterface("You hit for "+_values.get(0)+" damage.",GameEngine.MSG_COMBAT,false);break;
				}
				case 36:{
					sendToInterface(_values.get(0)+" hit you for "+_values.get(1)+" damage.",GameEngine.MSG_COMBAT,false);break;
				}
				case 41:{
					sendToInterface("You carefully nock an arrow.",GameEngine.MSG_COMBAT,false); break;
				}
				case 42:{
					sendToInterface("You have avoided "+_values.get(0)+"'s attack.",GameEngine.MSG_COMBAT,false);break;
				}
				case 43:{
					sendToInterface("You have missed.",GameEngine.MSG_COMBAT,false);break;
				}
				case 44:{
					sendToInterface("Critical hit!",GameEngine.MSG_COMBAT,false);break;
				}
				case 46:{
					sendToInterface("You use "+_values.get(0)+".",GameEngine.MSG_COMBAT,false);	break;
				}
				case 48:{
					sendToInterface(_values.get(0)+" is not available at this time: being prepared for reuse.",GameEngine.MSG_COMBAT,false);
					break;
				}
				case 49:{
					sendToInterface("You have equipped your "+_values.get(0),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 50:{
					sendToInterface("Your target cannot be found.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 51:{
					sendToInterface("You cannot use this on yourself.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 52:{
					sendToInterface("You have earned "+_values.get(0)+" adena.",GameEngine.MSG_SYSTEM_NORMAL,false); break;
				}
				case 53:{
					sendToInterface("You earned "+_values.get(0)+" "+_values.get(1),GameEngine.MSG_SYSTEM_NORMAL,false);	break;
				}
				case 54:{
					sendToInterface("You have earned "+_values.get(0),GameEngine.MSG_SYSTEM_NORMAL,false);	break;
				}
				case 55:{
					sendToInterface("You have failed to pick up "+_values.get(0)+" adena.",GameEngine.MSG_SYSTEM_NORMAL,false);	break;
				}
				case 56:{
					sendToInterface("You have failed to pick up "+_values.get(0),GameEngine.MSG_SYSTEM_NORMAL,false); break;
				}
				case 57:{
					sendToInterface("You have failed to pick up "+_values.get(0)+" "+_values.get(1),GameEngine.MSG_SYSTEM_NORMAL,false); break;
				}
				case 61:{
					sendToInterface("Nothing happened.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 62:{
					sendToInterface("Your "+_values.get(0)+" has been successfully enchanted.",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 63:{
					sendToInterface("Your +"+_values.get(0)+" "+_values.get(1)+" has been successfully enchanted.",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 64:{
					sendToInterface("The enchantment has failed! Your "+_values.get(0)+" has been crystallized.",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 65:{
					sendToInterface("The enchantment has failed! Your +"+_values.get(0)+" "+_values.get(1)+" has been crystallized.",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 66:{
					sendToInterface(_values.get(0)+" has invited you to his/her party. Do you accept the invitation?",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 67:{
					sendToInterface(_values.get(0)+" has invited you to the join the clan, "+_values.get(1)+" Do you wish to join?",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 79:{
					sendToInterface("This name already exists.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 80:{
					sendToInterface("Names must be between 1-16 characters, excluding spaces or special characters.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 84:{
					sendToInterface("You may not attack in a peaceful zone.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 85:{
					sendToInterface("You may not attack this target in a peaceful zone.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 92:{
					sendToInterface(_values.get(0)+" has worn off.",GameEngine.MSG_COMBAT,false);break;
				}
				case 95:{
					sendToInterface("You have earned "+_values.get(0)+" experience and "+_values.get(1)+" SP.",GameEngine.MSG_COMBAT,false);break;
				}
				case 96:{
					sendToInterface("Your level has increased!",GameEngine.MSG_COMBAT,true);break;
				}
				case 98:{
					sendToInterface("This item cannot be discarded.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 101:{
					sendToInterface("You cannot exit while in combat.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 102:{
					sendToInterface("You cannot restart while in combat.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 104:{
					sendToInterface("You may not equip items while casting or performing a skill.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 105:{
					sendToInterface("You have invited "+_values.get(0)+" to your party.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 106:{
					sendToInterface("You have joined "+_values.get(0)+"'s party.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 107:{
					sendToInterface(_values.get(0)+" has joined the party.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 108:{
					sendToInterface(_values.get(0)+" has left the party.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 109:{
					sendToInterface("Invalid target.",GameEngine.MSG_COMBAT,false);break;
				}
				case 110:{
					sendToInterface("The effects of "+_values.get(0)+" flow through you.",GameEngine.MSG_COMBAT,false);
					break;
				}
				case 111:{
					sendToInterface("Your shield defense has succeeded.",GameEngine.MSG_COMBAT,false);break;
				}
				case 112:{
					sendToInterface("You have run out of arrows.",GameEngine.MSG_COMBAT,false);break;
				}
				case 113:{
					sendToInterface(_values.get(0)+" cannot be used due to unsuitable terms.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 114:{
					sendToInterface("You have entered the shadow of the Mother Tree.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 115:{
					sendToInterface("You have left the shadow of the Mother Tree.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 118:{
					sendToInterface("You have requested a trade with "+_values.get(0),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 119:{
					sendToInterface(_values.get(0)+" has denied your request to trade.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 120:{
					sendToInterface("You begin trading with "+_values.get(0),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 121:{
					sendToInterface(_values.get(0)+" has confirmed the trade.",GameEngine.MSG_SYSTEM_NORMAL,false);
					_clientThread.getVisualInterface().procConfirmedTrade();break;
				}
				case 123:{
					sendToInterface("Your trade is successful.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 124:{
					sendToInterface(_values.get(0)+" has canceled the trade.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 129:{
					sendToInterface("Your inventory is full.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 132:{
					sendToInterface(_values.get(0)+" has been added to your friends list.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 133:{
					sendToInterface(_values.get(0)+" has been removed from your friends list.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 134:{
					sendToInterface("Please check your friends list again.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 135:{
					sendToInterface(_values.get(0)+" did not reply to your invitation), your invite has been canceled.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 136:{
					sendToInterface("You have not replied to "+_values.get(0)+"'s invitation), the offer has been canceled.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 139:{
					sendToInterface(_values.get(0)+" has resisted your "+_values.get(1),GameEngine.MSG_COMBAT,false);break;
				}
				case 140:{
					sendToInterface("Your skill was removed due to a lack of MP.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 142:{
					sendToInterface("You are already trading with someone.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 144:{
					sendToInterface("That is the incorrect target.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 145:{
					sendToInterface("Target is not found in the game.",GameEngine.MSG_CHAT_ALL,false);break;
				}
				case 148:{
					sendToInterface("You cannot use quest items.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 149:{
					sendToInterface("You cannot pick up or use items while trading.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 151:{
					sendToInterface("That is too far from you to discard.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 152:{
					sendToInterface("You have invited the wrong target.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 153:{
					sendToInterface(_values.get(0)+" is busy. Please try again later.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 154:{
					sendToInterface("Only the leader can give out invitations.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 155:{
					sendToInterface("The party is full.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 156:{
					sendToInterface("Drain was only half successful.",GameEngine.MSG_COMBAT,false);break;
				}
				case 157:{
					sendToInterface("You resisted "+_values.get(0)+"'s drain.",GameEngine.MSG_COMBAT,false);break;
				}
				case 158:{
					sendToInterface("Your attack has failed.",GameEngine.MSG_COMBAT,false);break;
				}
				case 159:{
					sendToInterface("You have resisted "+_values.get(0)+"'s magic.",GameEngine.MSG_COMBAT,false);break;
				}
				case 160:{
					sendToInterface(_values.get(0)+" is a member of another party and cannot be invited.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 161:{
					sendToInterface("That player is not currently online.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 164:{
					sendToInterface("Waiting for another reply.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 165:{
					sendToInterface("Waiting for another reply.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 166:{
					sendToInterface("Friend list is not ready yet. Please register again later.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 167:{
					sendToInterface(_values.get(0)+" is already on your friend list.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 168:{
					sendToInterface(_values.get(0)+" has requested to become friends.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 170:{
					sendToInterface("The user who requested to become friends is not found in the game.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 171:{
					sendToInterface(_values.get(0)+" is not on your friend list.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 176:{
					sendToInterface("That person is in message refusal mode.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 177:{
					sendToInterface("Message refusal mode.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 178:{
					sendToInterface("Message acceptance mode.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 181:{
					sendToInterface("Cannot see target.",GameEngine.MSG_COMBAT,false);break;
				}
				case 189:{
					sendToInterface("Your clan has been created.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 190:{
					sendToInterface("You have failed to create a clan.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 191:{
					sendToInterface("Clan member "+_values.get(0)+" has been expelled.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 193:{
					sendToInterface("Clan has dispersed.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 195:{
					sendToInterface("Entered the clan.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 196:{
					sendToInterface(_values.get(0)+" declined your clan invitation.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 197:{
					sendToInterface("You have withdrawn from the clan.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 199:{
					sendToInterface("You have recently been dismissed from a clan. You are not allowed to join another clan for 24-hours.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 200:{
					sendToInterface("You have withdrawn from the party.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 203:{
					sendToInterface("The party has dispersed.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 212:{
					sendToInterface("You are not a clan member and cannot perform this action.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 214:{
					sendToInterface("Your title has been changed.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 215:{
					sendToInterface("War with the "+_values.get(0)+" clan has begun.",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 216:{
					sendToInterface("War with the "+_values.get(0)+" clan has ended.",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 217:{
					sendToInterface("You have won the war over the "+_values.get(0)+" clan!",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 218:{
					sendToInterface("You have surrendered to the "+_values.get(0)+" clan.",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 222:{
					sendToInterface(_values.get(0)+" has joined the clan.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 223:{
					sendToInterface(_values.get(0)+" has withdrawn from the clan.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 224:{
					sendToInterface(_values.get(0)+" did not respond: Invitation to the clan has been cancelled.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 225:{
					sendToInterface("You didn't respond to "+_values.get(0)+"'s invitation: joining has been cancelled.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 228:{
					sendToInterface("Request to end war has been denied.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 229:{
					sendToInterface("You do not meet the criteria in order to create a clan.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 230:{
					sendToInterface("You must wait 10 days before creating a new clan.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 231:{
					sendToInterface("After a clan member is dismissed from a clan, the clan must wait at least a day before accepting a new member.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 232:{
					sendToInterface("After leaving or having been dismissed from a clan, you must wait at least a day before joining another clan.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 233:{
					sendToInterface(_values.get(0)+" has withdrawn from the clan.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 234:{
					sendToInterface("The target must be a clan member.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 236:{
					sendToInterface("Only the clan leader is enabled.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 239:{
					sendToInterface("The clan leader cannot withdraw.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 242:{
					sendToInterface("You must select a target.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 250:{
					sendToInterface("You have personally surrendered to the "+_values.get(0)+" clan. You are no longer participating in this clan war.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 261:{
					sendToInterface("Clan name is invalid.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 262:{
					sendToInterface("Clan name's length is incorrect.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 263:{
					sendToInterface("You have already requested the dissolution of your clan.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 264:{
					sendToInterface("You cannot dissolve a clan while engaged in a war.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 265:{
					sendToInterface("You cannot dissolve a clan during a siege or while protecting a castle.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 266:{
					sendToInterface("You cannot dissolve a clan while owning a clan hall or castle.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 269:{
					sendToInterface("You cannot dismiss yourself.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 271:{
					sendToInterface("A player can only be granted a title if the clan is level 3 or above.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 272:{
					sendToInterface("A clan crest can only be registered when the clan's skill level is 3 or above.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 274:{
					sendToInterface("Your clan's skill level has increased.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 276:{
					sendToInterface("You do not have the necessary materials or prerequisites to learn this skill.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 277:{
					sendToInterface("You have learned "+_values.get(0),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 278:{
					sendToInterface("You do not have enough SP to learn this skill.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 279:{
					sendToInterface("You do not have enough adena.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 282:{
					sendToInterface("You have not deposited any items in your warehouse.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 283:{
					sendToInterface("You have entered a combat zone.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 284:{
					sendToInterface("You have left a combat zone.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 291:{
					sendToInterface("Clan "+_values.get(0)+" is victorious over "+_values.get(1)+"'s castle siege!",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 295:{
					sendToInterface(_values.get(0)+"'s siege was canceled because there were no clans that participated.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 296:{
					sendToInterface("You received "+_values.get(0)+" damage from taking a high fall.",GameEngine.MSG_COMBAT,false);break;
				}
				case 297:{
					sendToInterface("You have taken "+_values.get(0)+" damage because you were unable to breathe.",GameEngine.MSG_COMBAT,false);break;
				}
				case 298:{
					sendToInterface("You have dropped "+_values.get(0),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 299:{
					sendToInterface(_values.get(0)+" has obtained "+_values.get(1)+" "+_values.get(2),GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 300:{
					sendToInterface(_values.get(0)+" has obtained "+_values.get(1),GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 301:{
					sendToInterface(_values.get(0)+" "+_values.get(1)+" has disappeared.",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 302:{
					sendToInterface(_values.get(0)+" has disappeared.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 304:{
					sendToInterface("Clan member "+_values.get(0)+" has logged into game.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 305:{
					sendToInterface("The player declined to join your party.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 309:{
					sendToInterface("You have succeeded in expelling the clan member.",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 319:{
					sendToInterface("This door cannot be unlocked.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 320:{
					sendToInterface("You have failed to unlock the door.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 323:{
					sendToInterface("Your force has increased to "+_values.get(0)+" level.",GameEngine.MSG_COMBAT,false);break;
				}
				case 324:{
					sendToInterface("Your force has reached maximum capacity.",GameEngine.MSG_COMBAT,false);break;
				}
				case 335:{
					sendToInterface(_values.get(0)+" is aborted.",GameEngine.MSG_COMBAT,false);	break;
				}
				case 337:{
					sendToInterface("The soulshot you are attempting to use does not match the grade of your equipped weapon.",GameEngine.MSG_COMBAT,false);	break;
				}
				case 338:{
					sendToInterface("You do not have enough soulshots for that.",GameEngine.MSG_COMBAT,false);break;
				}
				case 339:{
					sendToInterface("Cannot use soulshots.",GameEngine.MSG_COMBAT,false);break;
				}
				case 342:{
					sendToInterface("Power of spirits enabled.",GameEngine.MSG_COMBAT,false);break;
				}
				case 343:{
					sendToInterface("Sweeper failed, target not spoiled.",GameEngine.MSG_COMBAT,false);break;
				}
				case 351:{
					sendToInterface("Incorrect item count.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 355:{
					sendToInterface("Inappropriate enchant conditions.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 357:{
					sendToInterface("It has already been spoiled.",GameEngine.MSG_COMBAT,false);break;
				}
				case 361:{
					sendToInterface("Over-hit!",GameEngine.MSG_COMBAT,false);break;
				}
				case 362:{
					sendToInterface("You have acquired "+_values.get(0)+" bonus experience from a successful over-hit.",GameEngine.MSG_COMBAT,false);break;
				}
				case 368:{
					sendToInterface("Equipped +"+_values.get(0)+" "+_values.get(1),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 369:{
					sendToInterface("You have obtained a +"+_values.get(0)+" "+_values.get(1),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 371:{
					sendToInterface("Acquired +"+_values.get(0)+" "+_values.get(1),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 378:{
					sendToInterface(_values.get(0)+" purchased "+_values.get(1),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 379:{
					sendToInterface(_values.get(0)+" purchased +"+_values.get(1)+" "+_values.get(2),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 380:{
					sendToInterface(_values.get(0)+" purchased "+_values.get(1)+" "+_values.get(2)+"(s).",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 381:{
					sendToInterface("The game client encountered an error and was unable to connect to the petition server.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 387:{
					sendToInterface("This ends the GM petition consultation. Please take a moment to provide feedback about this service.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 388:{
					sendToInterface("Not under petition consultation.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 389:{
					sendToInterface("Your petition application has been accepted. - Receipt No. is "+_values.get(0)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 390:{
					sendToInterface("You may only submit one petition (active) at a time.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 391:{
					sendToInterface("Receipt No."+_values.get(0)+", petition cancelled.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 393:{
					sendToInterface("Failed to cancel petition. Please try again later.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 394:{
					sendToInterface("Petition consultation with "+_values.get(0)+", under way.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 395:{
					sendToInterface("Ending petition consultation with "+_values.get(0),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 404:{
					sendToInterface("Your Create Item level is too low to register this recipe.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 406:{
					sendToInterface("Petition application accepted.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 407:{
					sendToInterface("Petition under process.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 417:{
					sendToInterface(_values.get(0)+" has been disarmed.",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 422:{
					sendToInterface("You have exceeded the weight limit.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 423:{
					sendToInterface("You have cancelled the enchanting process.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 464:{
					sendToInterface("This feature is only available alliance leaders.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 465:{
					sendToInterface("You are not currently allied with any clans.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 466:{
					sendToInterface("You have exceeded the limit.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 467:{
					sendToInterface("You may not accept any clan within a day after expelling another clan.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 468:{
					sendToInterface("A clan that has withdrawn or been expelled cannot enter into an alliance within one day of withdrawal or expulsion.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 469:{
					sendToInterface("You may not ally with a clan you are currently at war with. That would be diabolical and treacherous.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 470:{
					sendToInterface("Only the clan leader may apply for withdrawal from the alliance.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 471:{
					sendToInterface("Alliance leaders cannot withdraw.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 473:{
					sendToInterface("Different alliance.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 474:{
					sendToInterface("That clan does not exist.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 477:{
					sendToInterface("No response. Invitation to join an alliance has been cancelled.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 478:{
					sendToInterface("No response. Your entrance to the alliance has been cancelled.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 479:{
					sendToInterface(_values.get(0)+" has joined as a friend.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 481:{
					sendToInterface(_values.get(0)+" has been deleted from your friends list.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 482:{
					sendToInterface("You cannot add yourself to your own friend list.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 484:{
					sendToInterface("This player is already registered in your friends list.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 485:{
					sendToInterface("No new friend invitations may be accepted.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 486:{
					sendToInterface("The following user is not in your friends list.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 487:{
					sendToInterface("======<Friends List>======",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 488:{
					sendToInterface(_values.get(0)+" (Currently: Online)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 489:{
					sendToInterface(_values.get(0)+" (Currently: Offline)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 490:{
					sendToInterface("========================",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 491:{
					sendToInterface("=======<Alliance Information>=======",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 492:{
					sendToInterface("Alliance Name: "+_values.get(0),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 493:{
					sendToInterface("Connection: "+_values.get(0)+" / Total "+_values.get(1),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 494:{
					sendToInterface("Alliance Leader: "+_values.get(0)+" of "+_values.get(1),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 495:{
					sendToInterface("Affiliated clans: Total "+_values.get(0)+" clan(s)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 496:{
					sendToInterface("=====<Clan Information>=====",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 497:{
					sendToInterface("Clan Name: "+_values.get(0),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 498:{
					sendToInterface("Clan Leader: "+_values.get(0),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 499:{
					sendToInterface("Clan Level: "+_values.get(0),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 500:{
					sendToInterface("------------------------",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 501:{
					sendToInterface("========================",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 502:{
					sendToInterface("You already belong to another alliance.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 503:{
					sendToInterface(_values.get(0)+" (Friend) has logged in.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 504:{
					sendToInterface("Only clan leaders may create alliances.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 505:{
					sendToInterface("You cannot create a new alliance within 10 days after dissolution.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 506:{
					sendToInterface("Incorrect alliance name. Please try again.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 507:{
					sendToInterface("Incorrect length for an alliance name.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 508:{
					sendToInterface("This alliance name already exists.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 509:{
					sendToInterface("Cannot accept. clan ally is registered as an enemy during siege battle.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 510:{
					sendToInterface("You have invited someone to your alliance.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 511:{
					sendToInterface("You must first select a user to invite.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 512:{
					sendToInterface("Do you really wish to withdraw from the alliance?",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 513:{
					sendToInterface("Enter the name of the clan you wish to expel.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 514:{
					sendToInterface("Do you really wish to dissolve the alliance?",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 515:{
					sendToInterface("Enter a file name for the alliance crest.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 516:{
					sendToInterface(_values.get(0)+"  has invited you to be their friend.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 517:{
					sendToInterface("You have accepted the alliance.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 518:{
					sendToInterface("You have failed to invite a clan into the alliance.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 519:{
					sendToInterface("You have withdrawn from the alliance.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 520:{
					sendToInterface("You have failed to withdraw from the alliance.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 521:{
					sendToInterface("You have succeeded in expelling a clan.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 522:{
					sendToInterface("You have failed to expel a clan.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 523:{
					sendToInterface("The alliance has been dissolved.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 524:{
					sendToInterface("You have failed to dissolve the alliance.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 525:{
					sendToInterface("You have succeeded in inviting a friend to your friends list.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 526:{
					sendToInterface("You have failed to add a friend to your friends list.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 527:{
					sendToInterface(_values.get(0)+"  leader, "+_values.get(1)+", has requested an alliance.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 528:{
					sendToInterface("Unable to find file at target location.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 530:{
					sendToInterface("The Spiritshot does not match the weapon's grade.",GameEngine.MSG_COMBAT,false);break;
				}
				case 531:{
					sendToInterface("You do not have enough Spiritshots for that.",GameEngine.MSG_COMBAT,false);break;
				}
				case 532:{
					sendToInterface("You may not use Spiritshots.",GameEngine.MSG_COMBAT,false);break;
				}
				case 533:{
					sendToInterface("Power of Mana enabled.",GameEngine.MSG_COMBAT,false);break;
				}
				case 535:{
					sendToInterface("Enter a name for your pet.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 538:{
					sendToInterface("Your SP has decreased by "+_values.get(0)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 539:{
					sendToInterface("Your Experience has decreased by "+_values.get(0)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 540:{
					sendToInterface("Clan leaders may not be deleted. Dissolve the clan first and try again.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 541:{
					sendToInterface("You may not delete a clan member. Withdraw from the clan first and try again.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 543:{
					sendToInterface("You already have a pet.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 544:{
					sendToInterface("Your pet cannot carry this item.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 547:{
					sendToInterface("Summoning your pet.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 548:{
					sendToInterface("Your pet's name can be up to 8 characters in length.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 549:{
					sendToInterface("To create an alliance, your clan must be Level 5 or higher.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 550:{
					sendToInterface("You may not create an alliance during the term of dissolution postponement.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 551:{
					sendToInterface("You cannot raise your clan level during the term of dispersion postponement.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 552:{
					sendToInterface("During the grace period for dissolving a clan, the registration or deletion of a clan's crest is not allowed.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 554:{
					sendToInterface("You cannot disperse the clans in your alliance.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 559:{
					sendToInterface("You have purchased "+_values.get(1)+" from "+_values.get(0)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 560:{
					sendToInterface(" You have purchased +"+_values.get(1)+" "+_values.get(2)+" from "+_values.get(0)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 561:{
					sendToInterface(" You have purchased "+_values.get(1)+" "+_values.get(2)+"(s) from "+_values.get(0)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 562:{
					sendToInterface("You may not crystallize this item. Your crystallization skill level is too low.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 568:{
					sendToInterface("Cubic Summoning failed.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 572:{
					sendToInterface("Do you wish to join "+_values.get(0)+"'s party? (Item distribution: Finders Keepers)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 573:{
					sendToInterface("Do you wish to join "+_values.get(0)+"'s party? (Item distribution: Random)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 574:{
					sendToInterface("Pets and Servitors are not available at this time.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 578:{
					sendToInterface("You cannot summon during combat.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 579:{
					sendToInterface("A pet cannot be sent back during battle.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 581:{
					sendToInterface("There is a space in the name.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 582:{
					sendToInterface("Inappropriate character name.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 583:{
					sendToInterface("Name includes forbidden words.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 584:{
					sendToInterface("This is already in use by another pet.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 589:{
					sendToInterface("A dead pet cannot be sent back.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 590:{
					sendToInterface("Your pet is motionless and any attempt you make to give it something goes unrecognized.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 591:{
					sendToInterface("An invalid character is included in the pet's name.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 594:{
					sendToInterface("You may not restore a hungry pet.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 600:{
					sendToInterface("You may not equip a pet item.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 601:{
					sendToInterface("There are "+_values.get(0)+" petitions currently on the waiting list.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 602:{
					sendToInterface("The petition system is currently unavailable. Please try again later.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 603:{
					sendToInterface("That item cannot be discarded or exchanged.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 607:{
					sendToInterface("You do not have any further skills to learn. Come back when you have reached Level "+_values.get(0),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 608:{
					sendToInterface(_values.get(0)+" has obtained "+_values.get(2)+" "+_values.get(1)+" by using Sweeper.",GameEngine.MSG_COMBAT,true);break;
				}
				case 609:{
					sendToInterface(_values.get(0)+" has obtained "+_values.get(1)+" by using Sweeper.",GameEngine.MSG_COMBAT,true);break;
				}
				case 610:{
					sendToInterface(" Your skill has been canceled due to lack of HP.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 612:{
					sendToInterface("The Spoil condition has been activated.",GameEngine.MSG_COMBAT,true);break;
				}
				case 613:{
					sendToInterface("======<Ignore List>======",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 615:{
					sendToInterface("You have failed to register the user to your Ignore List.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 617:{
					sendToInterface(_values.get(0)+" has been added to your Ignore List.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 618:{
					sendToInterface(_values.get(0)+" has been removed from your Ignore List.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 619:{
					sendToInterface(_values.get(0)+" has placed you on his/her Ignore List.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 626:{
					sendToInterface("The "+_values.get(0)+" clan did not respond: war proclamation has been refused.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 628:{
					sendToInterface("You have already been at war with the "+_values.get(0)+" clan: 5 days must pass before you can declare war again.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 642:{
					sendToInterface("You are already registered to the attacker side and must cancel your registration before submitting your request.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 650:{
					sendToInterface("You may not summon from your current location.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 661:{
					sendToInterface("This character cannot be spoiled.",GameEngine.MSG_COMBAT,true);break;
				}
				case 662:{
					sendToInterface("The other player is rejecting friend invitations.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 688:{
					sendToInterface("The clan that owns the castle is automatically registered on the defending side.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 672:{
					sendToInterface(_values.get(0)+" adena disappeared.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 681:{
					sendToInterface("The clan does not own a clan hall.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 683:{
					sendToInterface("There are no priority rights on a sweeper.",GameEngine.MSG_COMBAT,true);break;
				}
				case 691:{
					sendToInterface(_values.get(0)+"  clan is already a member of "+_values.get(1)+" alliance.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 692:{
					sendToInterface("The other party is frozen. Please wait a moment.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 695:{
					sendToInterface("You cannot set the name of the pet.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 700:{
					sendToInterface("The purchase is complete.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 702:{
					sendToInterface("There are no GMs currently visible in the public list as they may be performing other functions at the moment.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 703:{
					sendToInterface("======<GM List>======",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 704:{
					sendToInterface("GM :"+_values.get(0),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 707:{
					sendToInterface("You cannot teleport to a village that is in a siege.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 709:{
					sendToInterface("You do not have the right to use the clan warehouse.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 710:{
					sendToInterface("Only clans of clan level 1 or higher can use a clan warehouse.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 712:{
					sendToInterface("The siege of "+_values.get(0)+" has finished.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 714:{
					sendToInterface("A trap device has been tripped.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 720:{
					sendToInterface("The purchase price is higher than the amount of money that you have and so you cannot open a personal store.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 722:{
					sendToInterface("You cannot dissolve an alliance while an affiliated clan is participating in a siege battle.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 723:{
					sendToInterface("The opposing clan is participating in a siege battle.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 729:{
					sendToInterface("That item cannot be discarded.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 730:{
					sendToInterface("You have submitted your "+_values.get(0)+"th petition. - You may submit "+_values.get(1)+" more petition(s) today.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 733:{
					sendToInterface("We have received "+_values.get(0)+" petitions from you today and that is the maximum that you can submit in one day. You cannot submit any more petitions.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 736:{
					sendToInterface("The petition was canceled. You may submit "+_values.get(0)+" more petition(s) today.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 738:{
					sendToInterface("You have not submitted a petition.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 745:{
					sendToInterface("You are currently not in a petition chat.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 749:{
					sendToInterface("The effect of "+_values.get(0)+" has been removed.",GameEngine.MSG_COMBAT,true);break;
				}
				case 750:{
					sendToInterface("There are no other skills to learn.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 760:{
					sendToInterface(_values.get(0)+" cannot join the clan because one day has not yet passed since he/she left another clan.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 761:{
					sendToInterface(_values.get(0)+" clan cannot join the alliance because one day has not yet passed since it left another alliance.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 764:{
					sendToInterface("You have been playing for an extended period of time. Please consider taking a break.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 780:{
					sendToInterface("Observation is only possible during a siege.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 781:{
					sendToInterface("Observers cannot participate.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 783:{
					sendToInterface("Lottery ticket sales have been temporarily suspended.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 784:{
					sendToInterface("Lottery ticket sales have been temporarily suspended.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 794:{
					sendToInterface("You are not authorized to do that.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 797:{
					sendToInterface("You may create up to 48 macros.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 810:{
					sendToInterface("Invalid macro. Refer to the Help file for instructions.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 816:{
					sendToInterface("Tickets are now available for Monster Race "+_values.get(0)+"!",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 817:{
					sendToInterface("Now selling tickets for Monster Race "+_values.get(0)+"!",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 818:{
					sendToInterface("Ticket sales for the Monster Race will end in "+_values.get(0)+" minute(s).",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 819:{
					sendToInterface("Tickets sales are closed for Monster Race "+_values.get(0)+". Odds are posted.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 820:{
					sendToInterface("Tickets sales are closed for Monster Race "+_values.get(0)+". Odds are posted.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 821:{
					sendToInterface("Monster Race "+_values.get(0)+" will begin in 30 seconds!",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 822:{
					sendToInterface("Monster Race "+_values.get(0)+" is about to begin! Countdown in five seconds!",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 823:{
					sendToInterface("The race will begin in "+_values.get(0)+" second(s)!",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 824:{
					sendToInterface("They're off!",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 825:{
					sendToInterface("Monster Race "+_values.get(0)+" is finished!",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 826:{
					sendToInterface("First prize goes to the player in lane "+_values.get(0)+". Second prize goes to the player in lane "+_values.get(1),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 827:{
					sendToInterface("You may not impose a block on a GM.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 829:{
					sendToInterface("You cannot recommend yourself.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 830:{
					sendToInterface("You have recommended "+_values.get(0)+". You are authorized to make "+_values.get(1)+" more recommendations.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 831:{
					sendToInterface("You have been recommended by "+_values.get(0)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 832:{
					sendToInterface("That character has already been recommended.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 835:{
					sendToInterface("You may not throw the dice at this time. Try again later.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 836:{
					sendToInterface("You have exceeded your inventory volume limit and cannot take this item.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 837:{
					sendToInterface("Macro descriptions may contain up to 32 characters.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 838:{
					sendToInterface("Enter the name of the macro.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 840:{
					sendToInterface("That recipe is already registered.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 841:{
					sendToInterface("No further recipes may be registered.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 843:{
					sendToInterface(_values.get(0)+" has rolled "+_values.get(1),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 846:{
					sendToInterface("The siege of "+_values.get(0)+" has been canceled due to lack of interest.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 853:{
					sendToInterface("You may not alter your recipe book while engaged in manufacturing.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 871:{
					sendToInterface("The seed has been sown.",GameEngine.MSG_COMBAT,true);break;
				}
				case 872:{
					sendToInterface("This seed may not be sown here.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 873:{
					sendToInterface("That character does not exist.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 877:{
					sendToInterface("The symbol has been added.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 879:{
					sendToInterface("The manor system is currently under maintenance.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 880:{
					sendToInterface("The transaction is complete.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 881:{
					sendToInterface("There is a discrepancy on the invoice.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 882:{
					sendToInterface("The seed quantity is incorrect.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 883:{
					sendToInterface("The seed information is incorrect.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 884:{
					sendToInterface("The manor information has been updated.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 885:{
					sendToInterface("The number of crops is incorrect.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 886:{
					sendToInterface("The crops are priced incorrectly.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 887:{
					sendToInterface("The type is incorrect.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 888:{
					sendToInterface("No crops can be purchased at this time.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 889:{
					sendToInterface("The seed was successfully sown.",GameEngine.MSG_COMBAT,true);break;
				}
				case 890:{
					sendToInterface("The seed was not sown.",GameEngine.MSG_COMBAT,true);break;
				}
				case 891:{
					sendToInterface("You are not authorized to harvest.",GameEngine.MSG_COMBAT,true);break;
				}
				case 892:{
					sendToInterface("The harvest has failed.",GameEngine.MSG_COMBAT,true);break;
				}
				case 893:{
					sendToInterface("The harvest failed because the seed was not sown.",GameEngine.MSG_COMBAT,true);break;
				}
				case 894:{
					sendToInterface("Up to "+_values.get(0)+" recipes can be registered.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 898:{
					sendToInterface("Only characters of level 10 or above are authorized to make recommendations.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 899:{
					sendToInterface("The symbol cannot be drawn.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 910:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (Near Talking Island Village)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 911:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (Near Gludin Village)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 912:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (Near the Town of Gludio)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 913:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (Near the Neutral Zone)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 914:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (Near the Elven Village)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 915:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (Near the Dark Elf Village)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 916:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (Near the Town of Dion)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 917:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (Near the Floran Village)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 918:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (Near the Town of Giran)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 919:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (Near Giran Harbor)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 920:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (Near the Orc Village)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 921:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (Near the Dwarven Village)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 922:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (Near the Town of Oren)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 923:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (Near Hunters Village)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 924:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (Near Aden Castle Town)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 925:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (Near the Coliseum)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 926:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (Near Heine)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 927:{
					sendToInterface("The current time is :"+_values.get(0)+":"+_values.get(1)+" in the day.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 928:{
					sendToInterface("The current time is :"+_values.get(0)+":"+_values.get(1)+" in the night.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 930:{
					sendToInterface("Lottery tickets are not currently being sold.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 933:{
					sendToInterface("The seed pricing greatly differs from standard seed prices.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 935:{
					sendToInterface("The amount is not sufficient and so the manor is not in operation.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 936:{
					sendToInterface("Use "+_values.get(0)+".",GameEngine.MSG_COMBAT,false);break;
				}
				case 938:{
					sendToInterface("The community server is currently offline.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 970:{
					sendToInterface(_values.get(1)+"'s MP has been drained by "+_values.get(0)+".",GameEngine.MSG_COMBAT,false);break;
				}
				case 971:{
					sendToInterface("Petitions cannot exceed 255 characters.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 972:{
					sendToInterface("This pet cannot use this item.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 974:{
					sendToInterface("The soul crystal succeeded in absorbing a soul.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 975:{
					sendToInterface("The soul crystal was not able to absorb a soul.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 976:{
					sendToInterface("The soul crystal broke because it was not able to endure the soul energy.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 977:{
					sendToInterface("The soul crystals caused resonation and failed at absorbing a soul.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 978:{
					sendToInterface("The soul crystal is refusing to absorb a soul.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1010:{
					sendToInterface("A dead strider cannot be ridden.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1011:{
					sendToInterface("A strider in battle cannot be ridden.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1012:{
					sendToInterface("A strider cannot be ridden while in battle.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1013:{
					sendToInterface("A strider can be ridden only when standing.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1014:{
					sendToInterface("Your pet gained "+_values.get(0)+" experience points.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1015:{
					sendToInterface("Your pet hit for "+_values.get(0)+" damage.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1016:{
					sendToInterface("Your pet received "+_values.get(1)+" damage caused by "+_values.get(0)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1017:{
					sendToInterface("Pet's critical hit!",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1026:{
					sendToInterface("The summoned monster gave damage of "+_values.get(0),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1027:{
					sendToInterface("The summoned monster received damage of "+_values.get(1)+" caused by "+_values.get(0)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1028:{
					sendToInterface("Summoned monster's critical hit!",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1030:{
					sendToInterface("<Party Information>",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1031:{
					sendToInterface("Looting method: Finders keepers",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1032:{
					sendToInterface("Looting method: Random",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1033:{
					sendToInterface("Looting method: Random including spoil",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1034:{
					sendToInterface("Looting method: By turn",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1035:{
					sendToInterface("Looting method: By turn including spoil",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1036:{
					sendToInterface("You have exceeded the quantity that can be inputted.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1039:{
					sendToInterface("Items left at the clan hall warehouse can only be retrieved by the clan leader Do you want to continue?",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1041:{
					sendToInterface("The next seed purchase price is "+_values.get(0)+" adena.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1044:{
					sendToInterface("Monster race payout information is not available while tickets are being sold.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1046:{
					sendToInterface("Monster race tickets are no longer available.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1050:{
					sendToInterface("There are no communities in my clan. Clan communities are allowed for clans with skill levels of 2 and higher.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1051:{
					sendToInterface("Payment for your clan hall has not been made please make payment tomorrow.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1052:{
					sendToInterface("Payment of Clan Hall is overdue the owner loose Clan Hall.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1053:{
					sendToInterface("It is not possible to resurrect in battlefields where a siege war is taking place.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1058:{
					sendToInterface("The sales price for seeds is "+_values.get(0)+" adena.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1060:{
					sendToInterface("The remainder after selling the seeds is "+_values.get(0)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1061:{
					sendToInterface("The recipe cannot be registered. You do not have the ability to create items.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1064:{
					sendToInterface("The equipment, +"+_values.get(0)+"  "+_values.get(1)+", has been removed.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1065:{
					sendToInterface("While operating a private store or workshop, you cannot discard, destroy, or trade an item.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1066:{
					sendToInterface(_values.get(0)+" HP has been restored.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1067:{
					sendToInterface(_values.get(1)+" HP has been restored by "+_values.get(0),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1068:{
					sendToInterface(_values.get(0)+" MP has been restored.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1069:{
					sendToInterface(_values.get(1)+" MP has been restored by "+_values.get(0),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1112:{
					sendToInterface("The prize amount for the winner of Lottery #"+_values.get(0)+" is "+_values.get(1)+" adena. We have "+_values.get(2)+" first prize winners.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1113:{
					sendToInterface("The prize amount for Lucky Lottery #"+_values.get(0)+" is "+_values.get(1)+" adena. There was no first prize winner in this drawing, therefore the jackpot will be added to the next drawing.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1114:{
					sendToInterface("Your clan may not register to participate in a siege while under a grace period of the clan's dissolution.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1116:{
					sendToInterface("One cannot leave one's clan during combat.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1117:{
					sendToInterface("A clan member may not be dismissed during combat.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1118:{
					sendToInterface("Progress in a quest is possible only when your inventory's weight and volume are less than 80 percent of capacity.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1125:{
					sendToInterface("An item may not be created while engaged in trading.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1131:{
					sendToInterface("It is now midnight and effect of Shadow Sence can be felt.",GameEngine.MSG_CHAT_ALL,true);break;
				}
				case 1132:{
					sendToInterface("It is dawn and the effect of Shadow Sense will now disappear.",GameEngine.MSG_CHAT_ALL,true);break;
				}
				case 1135:{
					sendToInterface("While you are engaged in combat, you cannot operate a private store or private workshop.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1137:{
					sendToInterface(_values.get(0)+" harvested "+_values.get(2)+" "+_values.get(1)+"(s).",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1138:{
					sendToInterface(_values.get(0)+" harvested "+_values.get(2)+"(s).",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1176:{
					sendToInterface("This is a quest event period.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1177:{
					sendToInterface("This is the seal validation period.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1183:{
					sendToInterface("This is the initial period.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1184:{
					sendToInterface("This is a period of calculating statistics in the server.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1188:{
					sendToInterface("Your selected target can no longer receive a recommendation.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1196:{
					sendToInterface("Your force has reached maximum capacity.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1197:{
					sendToInterface("Summoning a servitor costs "+_values.get(1)+" "+_values.get(0)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1200:{
					sendToInterface(_values.get(0)+" ("+_values.get(1)+" Alliance)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1202:{
					sendToInterface(_values.get(0)+" (No alliance exists)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1208:{
					sendToInterface(_values.get(0)+" died and dropped "+_values.get(2)+" "+_values.get(1)+".",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 1209:{
					sendToInterface("Congratulations. Your raid was successful.",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 1210:{
					sendToInterface("Seven Signs: The quest event period has begun. Visit a Priest of Dawn or Priestess of Dusk to participate in the event.",GameEngine.MSG_ANUNCEMENT,false);break;
				}
				case 1211:{
					sendToInterface("Seven Signs: The quest event period has ended. The next quest event will start in one week.",GameEngine.MSG_ANUNCEMENT,false);break;
				}
				case 1212:{
					sendToInterface("Seven Signs: The Lords of Dawn have obtained the Seal of Avarice.",GameEngine.MSG_ANUNCEMENT,false);break;
				}
				case 1213:{
					sendToInterface("Seven Signs: The Lords of Dawn have obtained the Seal of Gnosis.",GameEngine.MSG_ANUNCEMENT,false);break;
				}
				case 1214:{
					sendToInterface("Seven Signs: The Lords of Dawn have obtained the Seal of Strife.",GameEngine.MSG_ANUNCEMENT,false);break;
				}
				case 1215:{
					sendToInterface("Seven Signs: The Revolutionaries of Dusk have obtained the Seal of Avarice.",GameEngine.MSG_ANUNCEMENT,false);break;
				}
				case 1216:{
					sendToInterface("Seven Signs: The Revolutionaries of Dusk have obtained the Seal of Gnosis.",GameEngine.MSG_ANUNCEMENT,false);break;
				}
				case 1217:{
					sendToInterface("Seven Signs: The Revolutionaries of Dusk have obtained the Seal of Strife.",GameEngine.MSG_ANUNCEMENT,false);break;
				}
				case 1218:{
					sendToInterface("Seven Signs: The Seal Validation period has begun.",GameEngine.MSG_ANUNCEMENT,false);break;
				}
				case 1219:{
					sendToInterface("Seven Signs: The Seal Validation period has ended.",GameEngine.MSG_ANUNCEMENT,false);break;
				}
				case 1235:{
					sendToInterface("Do you wish to delete all your friends?",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1240:{
					sendToInterface("Seven Signs: The Revolutionaries of Dusk have won.",GameEngine.MSG_ANUNCEMENT,false);break;
				}
				case 1241:{
					sendToInterface("Seven Signs: The Lords of Dawn have won.",GameEngine.MSG_ANUNCEMENT,false);break;
				}
				case 1260:{
					sendToInterface("Seven Signs: Preparations have begun for the next quest event.",GameEngine.MSG_ANUNCEMENT,false);break;
				}
				case 1261:{
					sendToInterface("Seven Signs: The quest event period has begun. Speak with a Priest of Dawn or Dusk Priestess if you wish to participate in the event.",GameEngine.MSG_ANUNCEMENT,false);break;
				}
				case 1262:{
					sendToInterface("Seven Signs: Quest event has ended. Results are being tallied.",GameEngine.MSG_ANUNCEMENT,false);break;
				}
				case 1263:{
					sendToInterface("Seven Signs: This is the seal validation period. A new quest event period begins next Monday.",GameEngine.MSG_ANUNCEMENT,false);break;
				}
				case 1267:{
					sendToInterface("Your contribution score is increased by "+_values.get(0)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1269:{
					sendToInterface("The new sub class has been added.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1270:{
					sendToInterface("The transfer of sub class has been completed.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1273:{
					sendToInterface("You will participate in the Seven Signs as a member of the Lords of Dawn.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1274:{
					sendToInterface("You will participate in the Seven Signs as a member of the Revolutionaries of Dusk.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1275:{
					sendToInterface("You've chosen to fight for the Seal of Avarice during this quest event period.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1276:{
					sendToInterface("You've chosen to fight for the Seal of Gnosis during this quest event period.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1277:{
					sendToInterface("You've chosen to fight for the Seal of Strife during this quest event period.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1278:{
					sendToInterface("The NPC server is not operating at this time.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1279:{
					sendToInterface("Contribution level has exceeded the limit. You may not continue.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1280:{
					sendToInterface("Magic Critical Hit!",GameEngine.MSG_COMBAT,true);break;
				}
				case 1281:{
					sendToInterface("Your excellent shield defense was a success!",GameEngine.MSG_COMBAT,true);break;
				}
				case 1282:{
					sendToInterface("Your Karma has been changed to "+_values.get(0),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1286:{
					sendToInterface("(Until next Monday at 6:00 p.m.)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1287:{
					sendToInterface("(Until today at 6:00 p.m.)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1290:{
					sendToInterface("Although the seal was not owned, since 35 percent or more people have voted.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1291:{
					sendToInterface("Although the seal was owned during the previous period, because less than 10 percent of people have voted.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1292:{
					sendToInterface("Since the seal was not owned during the previous period, and since less than 35 percent of people have voted.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1294:{
					sendToInterface("The competition has ended in a tie. Therefore, nobody has been awarded the seal.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1295:{
					sendToInterface("Sub classes may not be created or changed while a skill is in use.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1301:{
					sendToInterface("Only a Lord of Dawn may use this.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1302:{
					sendToInterface("Only a Revolutionary of Dusk may use this.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1308:{
					sendToInterface("Congratulations - You've completed a class transfer!",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1384:{
					sendToInterface(_values.get(0)+" has become the party leader.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1399:{
					sendToInterface("Only the leader of the party can transfer party leadership to another player.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1400:{
					sendToInterface("Only the leader of the party can transfer party leadership to another player.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1401:{
					sendToInterface("Slow down.you are already the party leader.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1402:{
					sendToInterface("You may only transfer party leadership to another member of the party.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1403:{
					sendToInterface("You have failed to transfer the party leadership.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1405:{
					sendToInterface(_values.get(0)+" CPs have been restored.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1433:{
					sendToInterface("You will now automatically apply "+_values.get(0)+" to your target.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1434:{
					sendToInterface("You will no longer automatically apply "+_values.get(0)+" to your weapon.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1438:{
					sendToInterface("There is no skill that enables enchant.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1439:{
					sendToInterface("You do not have all of the items needed to enchant that skill.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1440:{
					sendToInterface("You have succeeded in enchanting the skill "+_values.get(0)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1441:{
					sendToInterface("Skill enchant failed. The skill will be initialized.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1443:{
					sendToInterface("You do not have enough SP to enchant that skill.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1444:{
					sendToInterface("You do not have enough experience (Exp) to enchant that skill.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1447:{
					sendToInterface("You cannot do that while fishing.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1448:{
					sendToInterface("Only fishing skills may be used at this time.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1449:{
					sendToInterface("You've got a bite!",GameEngine.MSG_COMBAT,false);break;
				}
				case 1450:{
					sendToInterface("That fish is more determined than you are - it spit the hook!",GameEngine.MSG_COMBAT,false);break;
				}
				case 1451:{
					sendToInterface("Your bait was stolen by that fish!",GameEngine.MSG_COMBAT,false);break;
				}
				case 1452:{
					sendToInterface("Baits have been lost because the fish got away.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1453:{
					sendToInterface("You do not have a fishing pole equipped.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1454:{
					sendToInterface("You must put bait on your hook before you can fish.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1455:{
					sendToInterface("You cannot fish while under water.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1456:{
					sendToInterface("You cannot fish while riding as a passenger of a boat - it's against the rules.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1457:{
					sendToInterface("You can't fish here.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1458:{
					sendToInterface("Your attempt at fishing has been cancelled.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1459:{
					sendToInterface("You do not have enough bait.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1460:{
					sendToInterface("You reel your line in and stop fishing.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1461:{
					sendToInterface("You cast your line and start to fish.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1462:{
					sendToInterface("You may only use the Pumping skill while you are fishing.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1463:{
					sendToInterface("You may only use the Reeling skill while you are fishing.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1464:{
					sendToInterface("The fish has resisted your attempt to bring it in.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1465:{
					sendToInterface("Your pumping is successful, causing "+_values.get(0)+" damage.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1466:{
					sendToInterface("You failed to do anything with the fish and it regains "+_values.get(0)+" HP.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1467:{
					sendToInterface("You reel that fish in closer and cause "+_values.get(0)+" damage.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1468:{
					sendToInterface("You failed to reel that fish in further and it regains "+_values.get(0)+" HP.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1469:{
					sendToInterface("You caught something!",GameEngine.MSG_COMBAT,false);break;
				}
				case 1470:{
					sendToInterface("You cannot do that while fishing.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1471:{
					sendToInterface("You cannot do that while fishing.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1472:{
					sendToInterface("You look oddly at the fishing pole in disbelief and realize that you can't attack anything with this.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1479:{
					sendToInterface("That is the wrong grade of soulshot for that fishing pole.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1490:{
					sendToInterface("Traded "+_values.get(1)+" of crop "+_values.get(0)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1491:{
					sendToInterface("Failed in trading "+_values.get(1)+" of crop "+_values.get(0)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1492:{
					sendToInterface("You will be moved to the Olympiad Stadium in "+_values.get(0)+" second(s).",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1493:{
					sendToInterface("Your opponent made haste with their tail between their legs, the match has been cancelled.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1494:{
					sendToInterface("Your opponent does not meet the requirements to do battle, the match has been cancelled.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1495:{
					sendToInterface("The Grand Olympiad match will start in "+_values.get(0)+" second(s).",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1496:{
					sendToInterface("The match has started, fight!",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1497:{
					sendToInterface("Congratulations "+_values.get(0)+", you win the match!",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1498:{
					sendToInterface("There is no victory, the match ends in a tie.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1499:{
					sendToInterface("You will be moved back to town in "+_values.get(0)+" second(s).",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1500:{
					sendToInterface("You cannot participate in the Grand Olympiad Games with a character in their subclass.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1501:{
					sendToInterface("Only Noblesse can participate in the Olympiad.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1502:{
					sendToInterface("You have already been registered in a waiting list of an event.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1503:{
					sendToInterface("You have been registered in the Grand Olympiad Games waiting list for a class specific match.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1504:{
					sendToInterface("You have been registered in the Grand Olympiad Games waiting list for a non-class specific match.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1505:{
					sendToInterface("You have been removed from the Grand Olympiad Games waiting list.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1506:{
					sendToInterface("You have been removed from the Grand Olympiad Games waiting list.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1507:{
					sendToInterface("You cannot equip that item in a Grand Olympiad Games match.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1508:{
					sendToInterface("You cannot use that item in a Grand Olympiad Games match.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1509:{
					sendToInterface("You cannot use that skill in a Grand Olympiad Games match.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1510:{
					sendToInterface(_values.get(0)+" is making an attempt at resurrection. Do you want to continue with this resurrection?",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1511:{
					sendToInterface("While a pet is attempting to resurrect, it cannot help in resurrecting its master.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1513:{
					sendToInterface("Resurrection has already been proposed.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1515:{
					sendToInterface("A pet cannot be resurrected while it's owner is in the process of resurrecting.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1516:{
					sendToInterface("The target is unavailable for seeding.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1517:{
					sendToInterface("Failed in Blessed Enchant. The enchant value of the item became 0.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1518:{
					sendToInterface("You do not meet the required condition to equip that item.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1527:{
					sendToInterface("Your pet was hungry so it ate "+_values.get(0)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1533:{
					sendToInterface("Attention: "+_values.get(0)+" picked up "+_values.get(1)+".",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 1534:{
					sendToInterface("Attention: "+_values.get(0)+" picked up +"+_values.get(10)+" "+_values.get(2)+".",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 1537:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (near Rune Village)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1538:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (near the Town of Goddard)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1557:{
					sendToInterface("Seed price should be more than "+_values.get(0)+" and less than "+_values.get(1)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1558:{
					sendToInterface("The quantity of seed should be more than "+_values.get(0)+" and less than "+_values.get(1)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1559:{
					sendToInterface("Crop price should be more than "+_values.get(0)+" and less than "+_values.get(1)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1560:{
					sendToInterface("The quantity of crop should be more than "+_values.get(0)+" and less than "+_values.get(1)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1561:{
					sendToInterface("The clan, "+_values.get(0)+", has declared a Clan War.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1562:{
					sendToInterface("A Clan War has been declared against the clan, "+_values.get(0)+". If you are killed during the Clan War by members of the opposing clan, you will only lose a quarter of the normal experience from death.",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 1564:{
					sendToInterface("A Clan War can be declared only if the clan is level three or above, and the number of clan members is fifteen or greater.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1565:{
					sendToInterface("A Clan War cannot be declared against a clan that does not exist!",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1566:{
					sendToInterface("The clan, "+_values.get(0)+", has decided to stop the war.",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 1567:{
					sendToInterface("The war against "+_values.get(0)+" Clan has been stopped.",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 1569:{
					sendToInterface("A declaration of Clan War against an allied clan can't be made.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1571:{
					sendToInterface("======<Clans You've Declared War On>======",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1572:{
					sendToInterface("======<Clans That Have Declared War On You>======",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1576:{
					sendToInterface("Pet uses the power of spirit.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1581:{
					sendToInterface("The Command Channel has been disbanded.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1587:{
					sendToInterface(_values.get(0)+"'s party has left the Command Channel.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1598:{
					sendToInterface("Soulshots and spiritshots are not available for a dead pet or servitor. Sad, isn't it?",GameEngine.MSG_COMBAT,false);break;
				}
				case 1604:{
					sendToInterface("While dressed in formal wear, you can't use items that require all skills and casting operations.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1605:{
					sendToInterface("* Here, you can buy only seeds of "+_values.get(0)+" Manor.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1606:{
					sendToInterface("Congratulations - You've completed the third-class transfer quest!",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 1607:{
					sendToInterface(_values.get(0)+" adena has been withdrawn to pay for purchasing fees.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1611:{
					sendToInterface("Leader: "+_values.get(0),GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1612:{
					sendToInterface("=====<War List>=====",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1638:{
					sendToInterface("You cannot fish while using a recipe book, private manufacture or private store.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1639:{
					sendToInterface("Period "+_values.get(0)+" of the Grand Olympiad Games has started!",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 1640:{
					sendToInterface("Period "+_values.get(0)+" of the Grand Olympiad Games has now ended.",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 1641:{
					sendToInterface("Sharpen your swords, tighten the stitchings in your armor, and make haste to a Grand Olympiad Manager! Battles in the Grand Olympiad Games are now taking place!",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 1642:{
					sendToInterface("Much carnage has been left for the cleanup crew of the Olympiad Stadium. Battles in the Grand Olympiad Games are now over!",GameEngine.MSG_SYSTEM_NORMAL,true);break;
				}
				case 1651:{
					sendToInterface("The Grand Olympiad Games are not currently in progress.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1655:{
					sendToInterface("You caught something smelly and scary, maybe you should throw it back!?",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1657:{
					sendToInterface(_values.get(0)+" has earned "+_values.get(1)+" points in the Grand Olympiad Games.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1658:{
					sendToInterface(_values.get(0)+" has lost "+_values.get(1)+" points in the Grand Olympiad Games.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1662:{
					sendToInterface("The fish are no longer biting here because you've caught too many! Try fishing in another location.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1663:{
					sendToInterface("The clan crest was successfully registered. Remember, only a clan that owns a clan hall or castle can have their crest displayed.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1664:{
					sendToInterface("The fish is resisting your efforts to haul it in! Look at that bobber go!",GameEngine.MSG_COMBAT,false);break;
				}
				case 1665:{
					sendToInterface("You've worn that fish out! It can't even pull the bobber under the water!",GameEngine.MSG_COMBAT,false);break;
				}
				case 1667:{
					sendToInterface("Lethal Strike!",GameEngine.MSG_COMBAT,true);break;
				}
				case 1668:{
					sendToInterface("Your lethal strike was successful!",GameEngine.MSG_COMBAT,true);break;
				}
				case 1669:{
					sendToInterface("There was nothing found inside of that.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1670:{
					sendToInterface("Due to your Reeling and/or Pumping skill being three or more levels higher than your Fishing skill, a 50 damage penalty will be applied.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1671:{
					sendToInterface("Your reeling was successful! (Mastery Penalty:"+_values.get(0)+" )",GameEngine.MSG_COMBAT,false);break;
				}
				case 1672:{
					sendToInterface("Your pumping was successful! (Mastery Penalty:"+_values.get(0)+" )",GameEngine.MSG_COMBAT,false);break;
				}
				case 1673:{
					sendToInterface("Your current record for this Grand Olympiad is "+_values.get(0)+" match(es), "+_values.get(1)+" win(s) and "+_values.get(2)+" defeat(s). You have earned "+_values.get(3)+" Olympiad Point(s).",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1675:{
					sendToInterface("A manor cannot be set up between 6 a.m. and 8 p.m.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1685:{
					sendToInterface("You are unable to equip this item when your PK count is greater than or equal to one.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1689:{
					sendToInterface("You have already joined the waiting list for a class specific match.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1690:{
					sendToInterface("You have already joined the waiting list for a non-class specific match.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1691:{
					sendToInterface("You can't join a Grand Olympiad Game match with that much stuff on you! Reduce your weight to below 80 percent full and request to join again!",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1692:{
					sendToInterface("You have changed from your main class to a subclass and therefore are removed from the Grand Olympiad Games waiting list.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1693:{
					sendToInterface("You may not observe a Grand Olympiad Games match while you are on the waiting list.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1700:{
					sendToInterface("You don't have enough spiritshots needed for a pet/servitor.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1701:{
					sendToInterface("You don't have enough soulshots needed for a pet/servitor.",GameEngine.MSG_COMBAT,false);break;
				}
				case 1714:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (Near the Town of Schuttgart)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1730:{
					sendToInterface("To establish a Clan Academy, your clan must be Level 5 or higher.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1734:{
					sendToInterface("To join a Clan Academy, characters must be Level 40 or below, not belong another clan and not yet completed their 2nd class transfer.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1735:{
					sendToInterface(_values.get(0)+" does not meet the requirements to join a Clan Academy.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1738:{
					sendToInterface("Your clan has already established a Clan Academy.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1741:{
					sendToInterface("Congratulations! The "+_values.get(0)+"'s Clan Academy has been created.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1748:{
					sendToInterface("Clan Academy member "+_values.get(0)+" has successfully completed the 2nd class transfer and obtained "+_values.get(1)+" Clan Reputation points.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1749:{
					sendToInterface("Congratulations! You will now graduate from the Clan Academy and leave your current clan. As a graduate of the academy, you can immediately join a clan as a regular member without being subject to any penalties",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1755:{
					sendToInterface(_values.get(1)+" has been designated as the apprentice of clan member "+_values.get(0)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1756:{
					sendToInterface("Your apprentice, "+_values.get(0)+", has logged in.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1757:{
					sendToInterface("Your apprentice, "+_values.get(0)+", has logged out.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1758:{
					sendToInterface("Your sponsor, "+_values.get(0)+", has logged in.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1759:{
					sendToInterface("Your sponsor, "+_values.get(0)+", has logged out.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1762:{
					sendToInterface("You do not have the right to dismiss an apprentice.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1763:{
					sendToInterface(_values.get(1)+", clan member "+_values.get(0)+"'s apprentice, has been removed.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1771:{
					sendToInterface("Now that your clan level is above Level 5, it can accumulate clan reputation points.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1772:{
					sendToInterface("Since your clan was defeated in a siege, "+_values.get(0)+" points have been deducted from your clan's reputation score and given to the opposing clan.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1773:{
					sendToInterface("Since your clan emerged victorious from the siege, "+_values.get(0)+" points have been added to your clan's reputation score.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1774:{
					sendToInterface("Your clan's newly acquired contested clan hall has added "+_values.get(0)+" points to your clan's reputation score.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1775:{
					sendToInterface("Clan member "+_values.get(0)+" was an active member of the highest-ranked party in the Festival of Darkness. "+_values.get(1)+" points have been added to your clan's reputation score.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1776:{
					sendToInterface("Clan member "+_values.get(0)+" was named a hero. "+_values.get(1)+" points have been added to your clan's reputation score.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1777:{
					sendToInterface("You have successfully completed a clan quest. "+_values.get(0)+" points have been added to your clan's reputation score.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1778:{
					sendToInterface("An opposing clan has captured your clan's contested clan hall. "+_values.get(0)+" points have been deducted from your clan's reputation score.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1779:{
					sendToInterface("After losing the contested clan hall, 300 points have been deducted from your clan's reputation score.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1780:{
					sendToInterface("Your clan has captured your opponent's contested clan hall. "+_values.get(0)+" points have been deducted from your opponent's clan reputation score.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1781:{
					sendToInterface("Your clan has added "+_values.get(0)+" points to its clan reputation score.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1782:{
					sendToInterface("Your clan member "+_values.get(0)+" was killed. "+_values.get(1)+" points have been deducted from your clan's reputation score and added to your opponent's clan reputation score.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1783:{
					sendToInterface("For killing an opposing clan member, "+_values.get(0)+" points have been deducted from your opponents' clan reputation score.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1784:{
					sendToInterface("Your clan has failed to defend the castle. "+_values.get(0)+" points have been deducted from your clan's reputation score and added to your opponents'.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1785:{
					sendToInterface("The clan you belong to has been initialized. "+_values.get(0)+" points have been deducted from your clan reputation score.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1786:{
					sendToInterface("Your clan has failed to defend the castle. "+_values.get(0)+" points have been deducted from your clan's reputation score.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1787:{
					sendToInterface(_values.get(0)+" points have been deducted from the clan's reputation score.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1788:{
					sendToInterface("The clan skill "+_values.get(0)+" has been added.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1789:{
					sendToInterface("Since the Clan Reputation Score has dropped to 0 or lower, your clan skill(s) will be de-activated.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1790:{
					sendToInterface("The conditions necessary to increase the clan's level have not been met.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1791:{
					sendToInterface("The conditions necessary to create a military unit have not been met.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1793:{
					sendToInterface("The Royal Guard of "+_values.get(0)+" have been created.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1794:{
					sendToInterface("The Knights of "+_values.get(0)+" have been created.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1795:{
					sendToInterface("The Royal Guard of "+_values.get(0)+" have been created.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1798:{
					sendToInterface("Clan lord privileges have been transferred to "+_values.get(0)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1813:{
					sendToInterface(_values.get(0)+" has "+_values.get(1)+" hour(s) of usage time remaining.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1814:{
					sendToInterface(_values.get(0)+" has "+_values.get(1)+" minute(s) of usage time remaining.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1815:{
					sendToInterface(_values.get(0)+" was dropped in the "+_values.get(1)+" region.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1816:{
					sendToInterface("The owner of "+_values.get(1)+"  has appeared in the "+_values.get(0)+" region.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1817:{
					sendToInterface(_values.get(1)+"'s owner has logged into the "+_values.get(0)+" region.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1818:{
					sendToInterface(_values.get(0)+" has disappeared.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1835:{
					sendToInterface(_values.get(0)+" is full and cannot accept additional clan members at this time.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1842:{
					sendToInterface(_values.get(0)+" wishes to summon you from "+_values.get(1)+". Do you accept?",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1843:{
					sendToInterface(_values.get(0)+" is engaged in combat and cannot be summoned.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1844:{
					sendToInterface(_values.get(0)+" is dead at the moment and cannot be summoned.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1850:{
					sendToInterface("The Captain of the Order of Knights cannot be appointed.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1851:{
					sendToInterface("The Captain of the Royal Guard cannot be appointed.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1852:{
					sendToInterface("The attempt to acquire the skill has failed because of an insufficient Clan Reputation Score.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1855:{
					sendToInterface("Another military unit is already using that name. Please enter a different name.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1860:{
					sendToInterface("The clan reputation score is to low.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1861:{
					sendToInterface("The clan's crest has been deleted.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1862:{
					sendToInterface("Clan skills will now be activated since the clan's reputation score is 0 or higher.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1867:{
					sendToInterface("Your opponent's MP was reduced by "+_values.get(0)+".",GameEngine.MSG_COMBAT,false);break;
				}
				case 1898:{
					sendToInterface(_values.get(0)+" is currently trading or operating a private store and cannot be summoned.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1899:{
					sendToInterface("Your target is in an area which blocks summoning.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1902:{
					sendToInterface("Incompatible item grade. This item cannot be used.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1911:{
					sendToInterface("You cannot summon players who are currently participating in the Grand Olympiad.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1916:{
					sendToInterface("Your Death Penalty is now level "+_values.get(0)+".",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1917:{
					sendToInterface("Your Death Penalty has been lifted.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1924:{
					sendToInterface("Current location :"+_values.get(0)+", "+_values.get(1)+", "+_values.get(2)+" (near the Primeval Isle)",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1926:{
					sendToInterface("There is no opponent to receive your challenge for a duel.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1927:{
					sendToInterface(_values.get(0)+" has been challenged to a duel.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1928:{
					sendToInterface(_values.get(0)+"'s party has been challenged to a duel.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1929:{
					sendToInterface(_values.get(0)+" has accepted your challenge to a duel. The duel will begin in a few moments.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1930:{
					sendToInterface("You have accepted "+_values.get(0)+"'s challenge to a duel. The duel will begin in a few moments.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				case 1931:{
					sendToInterface(_values.get(0)+" has declined your challenge to a duel.",GameEngine.MSG_SYSTEM_NORMAL,false);break;
				}
				default:
				{
					sendToInterface("U:"+_messageId,GameEngine.MSG_SYSTEM_NORMAL,false);
					break;
				}

			}
	}

	private static void sendToInterface(String s, int type, boolean bold)
	{
		_clientThread.getVisualInterface().putMessage(s,type,bold);
		_types.clear();
		_values.clear();
	}

	private static void addString(String text)
	{
		_types.add(new Integer(TYPE_TEXT));
		_values.add(text);
	}

	private static void  addNumber(int number)
	{
		_types.add(new Integer(TYPE_NUMBER));
		_values.add(new Integer(number));
	}

	private static void addNpcName(String npcName)
	{
		_types.add(new Integer(TYPE_NPC_NAME));
		_values.add(npcName);
	}

	private static void  addItemName(String itemName)
	{
		_types.add(new Integer(TYPE_ITEM_NAME));
		_values.add(itemName);
	}

	private static void  addSkillName(String skillName)
	{
		_types.add(new Integer(TYPE_SKILL_NAME));
		_values.add(skillName);
	}

}
